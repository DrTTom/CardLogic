package de.tautenhahn.collection.generic.process;

import static spark.Spark.before;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.persistence.WorkspacePersistence;
import spark.Request;
import spark.Response;
import spark.ResponseTransformer;
import spark.Spark;


/**
 * Feeds the RestServer with content.
 *
 * @author jean
 */
public class RestServer
{

  /**
   * All structured output top frontend is JSON.
   */
  static class JsonTransformer implements ResponseTransformer
  {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public String render(Object model)
    {
      return gson.toJson(model);
    }

  }

  private static final RestServer INSTANCE = new RestServer();

  private RestServer()
  {
    // no other instances allowed
  }

  /**
   * Stops the server
   */
  public void stop()
  {
    Spark.stop();
  }

  /**
   * Sets the supported routes.
   */
  public void start()
  {
    staticFiles.location("frontend");

    allowCrossSiteCalls();

    get("/search/:type", (req, resp) -> search(req, resp, false), new JsonTransformer());
    get("/view/:type/:key", this::view, new JsonTransformer());
    get("/download/*", this::download);

    get("/check/:type", (req, resp) -> search(req, resp, true), new JsonTransformer());
    post("/submit", this::submit, new JsonTransformer());

    post("/import/:collectionName", this::importCollection);
    get("/export/", this::export);

    // proof of concept for file upload:
    get("/importDefault", (request, response) -> {
      return "<html><body>"
             + "<form method='post' enctype='multipart/form-data' action='/import/testImport' >"
             + "<input type='file' name='uploaded_file'>" + "<button>Upload file</button>" + "</form>"
             + "</body></html>";
    });

    // exception handling during development
    exception(Exception.class, (exception, request, response) -> {
      exception.printStackTrace();
    });
  }

  private Object download(Request request, Response response) throws IOException
  {

    StringBuffer refb = new StringBuffer();
    Arrays.asList(request.splat()).forEach(s -> refb.append("/").append(s));
    String ref = refb.substring(1);
    try (InputStream src = ApplicationContext.getInstance().getPersistence().find(ref))
    {
      return doDownload(src, response);
    }
  }

  private Object doDownload(InputStream src, Response response) throws IOException
  {
    response.header("Content-Type", "application/octet-stream");
    response.header("Content-Disposition", "attachment");
    try (OutputStream dest = response.raw().getOutputStream();)
    {
      copy(src, dest);
      return null;
    }
  }

  private DescribedObject view(Request req, Response res)
  {
    return ProcessScheduler.getInstance().getView().getData(req.params(":type"), req.params(":key"));
  }

  private SubmissionResult submit(Request req, Response res)
  {
    Gson gson = new GsonBuilder().create();
    DescribedObject object = gson.fromJson(req.body(), DescribedObject.class);

    SubmissionProcess proc = ProcessScheduler.getInstance().getSubmission(object.getType());
    return proc.submit(object, false);
  }

  private String importCollection(Request req, Response res) throws IOException, ServletException
  {
    WorkspacePersistence persistence = (WorkspacePersistence)ApplicationContext.getInstance()
                                                                               .getPersistence();
    persistence.close();
    persistence.init(req.params("collectionName"));
    req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
    try (InputStream ins = req.raw().getPart("uploaded_file").getInputStream())
    {
      persistence.importZip(ins);
    }
    return "OK";
  }

  private String export(Request req, Response res) throws IOException
  {
    WorkspacePersistence persistence = (WorkspacePersistence)ApplicationContext.getInstance()
                                                                               .getPersistence();
    persistence.close();
    persistence.init(req.params("collectionName"));
    try (InputStream ins = req.raw().getInputStream())
    {
      persistence.importZip(ins);
    }
    return "OK";
  }

  private void allowCrossSiteCalls()
  {
    before((request, response) -> {
      response.header("Access-Control-Allow-Origin", "*");
      response.header("Access-Control-Request-Method", "*");
      response.header("Access-Control-Allow-Headers", "*");
      // Note: this may or may not be necessary in your particular application
      response.type("application/json");
    });

    options("/*", (request, response) -> {

      String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
      if (accessControlRequestHeaders != null)
      {
        response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
      }

      String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
      if (accessControlRequestMethod != null)
      {
        response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
      }

      return "OK";
    });
  }


  private SearchResult search(Request req, Response res, boolean strictCheck)
  {
    res.type("text/plain");
    res.header("Content-Type", "application/json; charset=UTF-8");
    String type = req.params(":type");
    SearchProcess proc = ProcessScheduler.getInstance().getSearch(type);
    Map<String, String> allParams = new Hashtable<>();
    req.queryParams().forEach(p -> allParams.put(p, req.queryParams(p)));
    return strictCheck ? proc.checkValues("TODO", allParams) : proc.search(allParams);
  }

  private static void copy(InputStream src, OutputStream dest) throws IOException
  {
    byte[] buf = new byte[1024];
    int count = 0;
    while ((count = src.read(buf)) != -1)
    {
      dest.write(buf, 0, count);
    }
  }

  public static RestServer getInstance()
  {
    return INSTANCE;
  }

}
