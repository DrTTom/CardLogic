package de.tautenhahn.collection.generic.process;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import de.tautenhahn.collection.cards.CardApplicationContext;
import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.SubmissionResponse;
import lombok.extern.slf4j.Slf4j;
import spark.Spark;


/**
 * Tests for calling the REST paths on the server.
 *
 * @author TT
 */
@Slf4j
public class TestRestServer
{

  private static final Gson GSON = new Gson();

  private static final HttpClient CLIENT = HttpClient.newBuilder().build();

  private static final String SERVER_URI = "http://localhost:4567";

  @BeforeAll
  static void setUp() throws IOException
  {
    CardApplicationContext.register();
    ApplicationContext.getInstance().getPersistence().init("cards");
    RestServer.getInstance().start();
    Spark.awaitInitialization();
    log.debug("Started server");
  }

  @AfterAll
  static void tearDown()
  {
    RestServer.getInstance().stop();
  }

  /**
   * Requests a static page from the server.
   *
   * @throws Exception all the unexpected Exceptions go to test protocol
   */
  @Test
  void staticPage() throws Exception
  {
    String index = callService(get(""), 200, String.class);
    assertThat(index).startsWith("<!DOCTYPE html>");
  }

  /**
   * Create, search, update, check and delete the most simple entity in the system.
   * 
   * @throws Exception all the unexpected Exceptions go to test protocol
   */
  @Test
  void crudSimpleType() throws Exception
  {

    Map<String, String> attribs = new HashMap<>();
    attribs.putAll(Map.of("fullName", "Ostermann AG", "remark", "test data"));
    DescribedObject data = new DescribedObject("maker", "OAG", attribs);
    SubmissionResponse response = callService(post("/collected/maker", data), 422, SubmissionResponse.class);
    assertThat(response.getMessage()).isEqualTo("msg.error.remainingProblems");

    data.getAttributes().putAll(Map.of("from", "1990", "to", "2001", "place", "Neverland", "domain", "WW"));
    response = callService(post("/collected/maker", data), 200, SubmissionResponse.class);
    String key = response.getPrimaryKey();

    SearchResult sr = callService(get("/collected/maker/search"), 200, SearchResult.class);
    var found = sr.getMatches().stream().filter(d -> d.getPrimKey().equals(key)).findAny().get();
    assertThat(found.getAttributes().get("fullName")).isEqualTo("Ostermann AG");

    found.getAttributes().put("from", "1798");
    var ur = callService(put("/collected/maker/key/" + key, found), 200, String.class);

    System.out.println(ur);

  }

  HttpRequest get(String path) throws URISyntaxException
  {
    return HttpRequest.newBuilder(new URI(SERVER_URI + path)).GET().build();
  }

  HttpRequest post(String path, Object value) throws URISyntaxException
  {
    String content = GSON.toJson(value);
    return HttpRequest.newBuilder(new URI(SERVER_URI + path))
                      .POST(HttpRequest.BodyPublishers.ofString(content))
                      .build();
  }

  HttpRequest put(String path, Object value) throws URISyntaxException
  {
    String content = GSON.toJson(value);
    return HttpRequest.newBuilder(new URI(SERVER_URI + path))
                      .PUT(HttpRequest.BodyPublishers.ofString(content))
                      .build();
  }

  <T> T callService(HttpRequest req, int expectedCode, Class<T> responseClass)
    throws IOException, InterruptedException
  {
    HttpResponse<String> response = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
    assertThat(response.statusCode()).as("status code").isEqualTo(expectedCode);
    return String.class == responseClass ? (T)response.body() : GSON.fromJson(response.body(), responseClass);
  }
}
