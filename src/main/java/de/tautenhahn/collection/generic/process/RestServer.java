package de.tautenhahn.collection.generic.process;

import static spark.Spark.delete;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.staticFiles;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.SubmissionResponse;
import de.tautenhahn.collection.generic.persistence.Persistence;
import de.tautenhahn.collection.generic.persistence.WorkspacePersistence;
import lombok.extern.slf4j.Slf4j;
import spark.Request;
import spark.Response;
import spark.ResponseTransformer;
import spark.Spark;


/**
 * Feeds the REST interface with content.
 *
 * @author TT
 */
@Slf4j
public class RestServer
{

  private static final Gson GSON = new GsonBuilder().create();

  private static final RestServer INSTANCE = new RestServer();

  public static final Type MAP_TYPE = new TypeToken<Map<String, String>>()
  {}.getType();

  private RestServer()
  {
    // no other instances allowed
  }

  /**
   * Singleton getter.
   */
  public static RestServer getInstance()
  {
    return INSTANCE;
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
    // To enable direct client editing without restart of server:
    staticFiles.externalLocation(Path.of("src/main/resources/client").toAbsolutePath().toString());
    // staticFiles.location("client");
    final JsonTransformer transformer = new JsonTransformer();

    post("/collected/:type", this::submit, transformer);

    get("/collected/:type/search", (req, resp) -> search(req, resp, false), transformer);

    get("/collected/:type/key/:key", this::view, transformer);
    put("/collected/:type/key/:key", this::update, transformer);
    delete("/collected/:type/key/:key", this::doDelete, transformer);

    get("/file/:ref", this::download);
    post("/file/:type", this::doUpload);

    post("/collection", this::importCollection);
    get("/collection", (req, resp) -> export(resp));

    get("/datatypes", (req, resp) -> ApplicationContext.getInstance().getPersistence().getObjectTypes());

    // end of new paths

    get("/download/*", this::download);

    // exception handling during development
    exception(Exception.class, (exception, request, response) -> log.error("Exception occurred", exception));
  }

  private String doDelete(Request req, Response res)
  {
    String msg = ProcessFactory.getInstance().getDelete().delete(req.params(":type"), req.params(":key"));
    switch (msg)
    {
      case "msg.error.notFound":
        res.status(404);
        break;
      case "msg.error.referenced":
        res.status(422);
        break;
      case "msg.ok.deletion":
        res.status(200);
        break;
      default:
        throw new IllegalStateException("unsupported case" + msg);
    }
    return msg;
  }

  private Object update(Request req, Response res)
  {
    Map<String, String> object = GSON.fromJson(req.body(), MAP_TYPE);

    SubmissionProcess proc = new SubmissionProcess(req.params(":type"));
    SubmissionResponse resp = proc.update(req.params(":key"),
                                          object,
                                          "true".equals(req.queryParams("lenient")));
    if (!resp.isSuccess())
    {
      res.status(422);
    }
    return resp;
  }

  /**
   * @param request
   * @param response not needed
   * @return message
   */
  private Object doUpload(Request request, Response response)
  {
    String type = request.params(":type");
    try
    {
      request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
      Part part = request.raw().getPart("file");
      Persistence me = ApplicationContext.getInstance().getPersistence();
      String ref = me.createNewBinRef(request.queryParamOrDefault("primKey", UUID.randomUUID().toString()),
                                      type,
                                      extensionOf(part.getSubmittedFileName()));
      try (InputStream ins = request.raw().getPart("file").getInputStream())
      {
        me.store(ins, ref);
        response.status(200);
        return ref;
      }
    }
    catch (IOException | ServletException | RuntimeException e)
    {
      // note that Spark will return wrong code if Exception is not caught here
      log.error("Exception occurred, causing HTTP 500", e);
      response.status(500);
      return "Internal server error";
    }
  }

  private String extensionOf(String fileName)
  {
    int pos = fileName.lastIndexOf('.');
    return pos > 0 && pos < fileName.length() - 1 ? fileName.substring(pos + 1) : "bin";
  }

  private Object download(Request request, Response response) throws IOException
  {
    String ref = splatParam(request);
    try (InputStream storedRes = ApplicationContext.getInstance().getPersistence().find(ref))
    {
      String type = "application/octet-stream";
      if (ref.toLowerCase(Locale.ENGLISH).endsWith(".jpg"))
      {
        type = "image/jpeg";
      }
      response.header("Content-Type", type);
      response.header("Content-Disposition", "attachment");
      try (OutputStream dest = response.raw().getOutputStream())
      {
        storedRes.transferTo(dest);
        return null;
      }
    }
  }

  private String splatParam(Request request)
  {
    StringBuffer refb = new StringBuffer();
    Arrays.asList(request.splat()).forEach(s -> refb.append("/").append(s));
    return refb.length() == 0 ? "" : refb.substring(1);
  }

  /**
   * @param req
   * @param res not needed
   */
  private DescribedObject view(Request req, Response res)
  {
    DescribedObject result = ProcessFactory.getInstance()
                                           .getView()
                                           .getData(req.params(":type"), req.params(":key"));
    if (result == null)
    {
      res.status(404);
    }
    return result;
  }

  /**
   * @param req
   * @param res
   */
  private SubmissionResponse submit(Request req, Response res)
  {
    Map<String, String> object = GSON.fromJson(req.body(), MAP_TYPE);

    SubmissionProcess proc = new SubmissionProcess(req.params(":type"));
    SubmissionResponse resp = proc.submit(object, "true".equals(req.queryParams("lenient")));
    if (!resp.isSuccess())
    {
      res.status(422);
    }
    return resp;
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
    try (InputStream insRes = req.raw().getPart("uploaded_file").getInputStream())
    {
      persistence.importZip(insRes);
    }
    res.status(200);
    return "OK";
  }

  /**
   * @param res
   * @throws IOException
   */
  private String export(Response res) throws IOException
  {
    ApplicationContext app = ApplicationContext.getInstance();
    WorkspacePersistence persistence = (WorkspacePersistence)app.getPersistence();
    persistence.close();
    res.header("Content-Type", "application/zip");
    res.header("Content-Disposition", "attachment");
    Map<String, Collection<String>> binRefs = new HashMap<>();
    persistence.getObjectTypes()
               .forEach(t -> binRefs.put(t, app.getInterpreter(t).getBinaryValuedAttributes()));
    try (OutputStream dest = res.raw().getOutputStream())
    {
      persistence.exportZip(binRefs, dest);
      return null;
    }
  }

  private SearchResult search(Request req, Response res, boolean strictCheck)
  {
    res.type("text/plain");
    res.header("Content-Type", "application/json; charset=UTF-8");
    String type = req.params(":type");
    SearchProcess proc = ProcessFactory.getInstance().getSearch(type);
    Map<String, String> allParams = new Hashtable<>();
    req.queryParams().forEach(p -> allParams.put(p, req.queryParams(p)));
    String primKey = allParams.remove("primKey");
    return proc.execute(allParams, primKey, strictCheck);
  }

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
}
