package de.tautenhahn.collection.process;

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

import javax.servlet.MultipartConfigElement;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.tautenhahn.collection.generic.ApplicationContext;
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

  public void stop()
  {
    Spark.stop();
  }

  public void start()
  {

    // staticFiles.externalLocation("searchResource");
    staticFiles.location("frontend");
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


    get("/view/:type/:key",
        (req, response) -> ProcessScheduler.getInstance().getView().getData(req.params(":type"),
                                                                            req.params(":key")),
        new JsonTransformer());

    get("/search/:type", this::search, new JsonTransformer());

    // proof of concept for file upload:
    get("/upload", (request, response) -> {
      return "<html><body>" + "<form method='post' enctype='multipart/form-data' action='/upload/someRef' >"
             + "<input type='file' name='uploaded_file'>" + "<button>Upload file</button>" + "</form>"
             + "<img src='http://localhost:4567/download/deck/0/88.jpg' alt='http://localhost:4567/download/deck/0/88.jpg'>"
             + "</body></html>";
    });
    post("/upload/:ref", (request, response) -> {
      request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
      byte[] buf = new byte[100];
      try (InputStream is = request.raw().getPart("uploaded_file").getInputStream())
      {
        is.read(buf);
      }
      return "File uploaded at " + request.params(":ref") + " started with " + new String(buf);
    });

    // proof of concept for file download:
    get("/download/*", (request, response) -> {
      StringBuffer refb = new StringBuffer();
      Arrays.asList(request.splat()).forEach(s -> refb.append("/").append(s));
      String ref = refb.substring(1);

      response.header("Content-Type", "application/octet-stream");
      response.header("Content-Disposition", "attachment");
      try (OutputStream dest = response.raw().getOutputStream();
        InputStream src = ApplicationContext.getInstance().getPersistence().find(ref))
      {
        copy(src, dest);
        return null;
      }

    });

    // proof of concept for serving static files not needed, just stream them

    // exception handling during development
    exception(Exception.class, (exception, request, response) -> {
      exception.printStackTrace();
    });
  }


  private Search search(Request req, Response res)
  {
    res.type("text/plain");
    res.header("Content-Type", "application/json; charset=UTF-8");
    String type = req.params(":type");
    SearchProcess proc = ProcessScheduler.getInstance().getCurrentSearch(type);
    req.queryParams().forEach(p -> proc.setAttribute(p, req.queryParams(p)));
    return proc.execute();
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
