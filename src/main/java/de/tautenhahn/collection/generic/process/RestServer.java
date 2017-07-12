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
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
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
 * @author TT
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
    post("/upload/*", this::doUpload);

    get("/check/:type", (req, resp) -> search(req, resp, true), new JsonTransformer());
    post("/submit", this::submit, new JsonTransformer());

    post("/import/:collectionName", this::importCollection);
    get("/export/", this::export);

    // exception handling during development
    exception(Exception.class, (exception, request, response) -> {
      exception.printStackTrace();
    });
  }

  /**
   * @param request
   * @param response not needed
   * @return message
   * @throws IOException
   * @throws ServletException
   */
  private Object doUpload(Request request, Response response) throws IOException, ServletException
  {
    String ref = splatParam(request);
    System.out.println("\n\n got upload to " + ref);
    request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
    try (InputStream ins = request.raw().getPart("file").getInputStream())
    {
      ApplicationContext.getInstance().getPersistence().store(ins, ref);
    }
    return "File uploaded to " + ref;
  }

  private Object download(Request request, Response response) throws IOException
  {

    String ref = splatParam(request);
    try (InputStream src = ApplicationContext.getInstance().getPersistence().find(ref))
    {
      return doDownload(src, response, ref);
    }
  }

  private String splatParam(Request request)
  {
    StringBuffer refb = new StringBuffer();
    Arrays.asList(request.splat()).forEach(s -> refb.append("/").append(s));
    String ref = refb.length() == 0 ? "" : refb.substring(1);
    return ref;
  }

  private Object doDownload(InputStream src, Response response, String ref) throws IOException
  {
    String type = "application/octet-stream";
    if (ref.toLowerCase(Locale.ENGLISH).endsWith(".jpg"))
    {
      type = "image/jpeg";
    }
    response.header("Content-Type", type);
    response.header("Content-Disposition", "attachment");
    try (OutputStream dest = response.raw().getOutputStream();)
    {
      copy(src, dest);
      return null;
    }
  }

  /**
   * @param req
   * @param res not needed
   */
  private DescribedObject view(Request req, Response res)
  {
    return ProcessFactory.getInstance().getView().getData(req.params(":type"), req.params(":key"));
  }

  /**
   * @param req
   * @param res not needed
   */
  private SubmissionResult submit(Request req, Response res)
  {
    Gson gson = new GsonBuilder().create();
    DescribedObject object = gson.fromJson(req.body(), DescribedObject.class);

    SubmissionProcess proc = ProcessFactory.getInstance().getSubmission(object.getType());
    return proc.submit(object, false);
  }

  /**
   * @param req
   * @param res not needed
   * @throws IOException
   * @throws ServletException
   */
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

  /**
   * @param req not needed
   * @param res
   * @throws IOException
   */
  private String export(Request req, Response res) throws IOException
  {
    ApplicationContext app = ApplicationContext.getInstance();
    WorkspacePersistence persistence = (WorkspacePersistence)app.getPersistence();
    persistence.close();
    res.header("Content-Type", "application/zip");
    res.header("Content-Disposition", "attachment");
    Map<String, Collection<String>> binRefs = new HashMap<>();
    persistence.getObjectTypes()
               .forEach(t -> binRefs.put(t, app.getInterpreter(t).getBinaryValuedAttributes()));
    try (OutputStream dest = res.raw().getOutputStream();)
    {
      persistence.exportZip(binRefs, dest);
      return null;
    }
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
    SearchProcess proc = ProcessFactory.getInstance().getSearch(type);
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

  /**
   * Singleton getter.
   */
  public static RestServer getInstance()
  {
    return INSTANCE;
  }

}
