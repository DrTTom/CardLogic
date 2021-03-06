package de.tautenhahn.collection.generic.process;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import de.tautenhahn.collection.cards.CardApplicationContext;
import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.SubmissionResponse;
import de.tautenhahn.collection.generic.data.question.Question;
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
    ApplicationContext.getInstance().getPersistence().init("cardsTest");
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
   * Perform exports, only checks that the obtained stuff looks like ZIP.
   */
  @Test
  void export() throws Exception
  {
    HttpResponse<byte[]> response = CLIENT.send(get("/collection"), HttpResponse.BodyHandlers.ofByteArray());
    assertThat(response.headers().allValues("Content-Type").get(0)).isEqualTo("application/zip");
    assertThat(new String(response.body(), 0, 2, StandardCharsets.UTF_8)).isEqualTo("PK");

    response = CLIENT.send(get("/collection?fileType=labels&objectType=deck"),
                           HttpResponse.BodyHandlers.ofByteArray());
    assertThat(response.headers().allValues("Content-Type").get(0)).isEqualTo("application/docx");
    assertThat(new String(response.body(), 0, 2, StandardCharsets.UTF_8)).isEqualTo("PK");
  }


  /**
   * Create, search, update, check and delete the most simple entity in the system.
   *
   * @throws Exception all the unexpected Exceptions go to test protocol
   */
  @Test
  void crudSimpleType() throws Exception
  {
    String key = createMaker();
    DescribedObject found = readMaker(key);
    updateMaker(found);
    deleteMaker(key);
  }

  /**
   * When creating an object, the data is checked for inconsistencies. Creation is done only for correct data.
   */
  private String createMaker() throws Exception
  {
    Map<String, String> data = new HashMap<>(Map.of("name", "Ostermann AG", "remark", "test data"));
    SubmissionResponse response = callService(post("/collected/maker", data), 422, SubmissionResponse.class);
    assertThat(response.getMessage()).startsWith("Der Datensatz ist noch fehlerhaft");

    data.putAll(Map.of("from", "1990", "to", "2001", "place", "Neverland", "domain", "WW"));
    response = callService(post("/collected/maker", data), 200, SubmissionResponse.class);
    return response.getPrimaryKey();
  }

  /**
   * Objects can be accessed for display. To enable editing an object, the client should send a search request
   * with found data. By that request the client obtains the questions and the set of similar objects.
   */
  private DescribedObject readMaker(String key) throws Exception
  {
    DescribedObject read = callService(get("/collected/maker/key/" + key), 200, DescribedObject.class);
    assertThat(read.getAttributes().get("name")).isEqualTo("Ostermann AG");

    SearchResult sr = callService(get("/collected/maker/search" + toQueryParams(read)),
                                  200,
                                  SearchResult.class);
    DescribedObject found = sr.getMatches().stream().filter(d -> d.getPrimKey().equals(key)).findAny().get();
    assertThat(found.getAttributes().get("name")).isEqualTo("Ostermann AG");
    return found;
  }

  /**
   * Update should also check the data for consistency.
   *
   * @param found
   * @throws Exception
   */
  private void updateMaker(DescribedObject found) throws Exception
  {
    found.getAttributes().put("from", "2010");
    SubmissionResponse ur = callService(put("/collected/maker/key/" + found.getPrimKey(),
                                            found.getAttributes()),
                                        422,
                                        SubmissionResponse.class);
    assertThat(ur.getQuestions()
                 .stream()
                 .filter(q -> "to".equals(q.getParamName()))
                 .findAny()
                 .map(Question::getProblem)
                 .get()).isEqualTo("msg.error.tooEarlyFor.maker.from");
    found.getAttributes().put("from", "1989");

    ur = callService(put("/collected/maker/key/" + found.getPrimKey(), found.getAttributes()),
                     200,
                     SubmissionResponse.class);
    assertThat(ur.getMessage()).contains("wurde erfolgreich gespeichert");

    DescribedObject changed = readMaker(found.getPrimKey());
    assertThat(changed.getAttributes().get("from")).isEqualTo("1989");
  }

  private void deleteMaker(String key) throws Exception
  {
    String path = "/collected/maker/key/" + key;
    callService(get(path), 200, String.class);
    callService(delete(path), 200, String.class);
    callService(get(path), 404, String.class);
  }

  private String toQueryParams(DescribedObject read)
  {
    return "?" + read.getAttributes()
                     .entrySet()
                     .stream()
                     .map(e -> e.getKey() + '=' + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                     .collect(Collectors.joining("&"));
  }

  HttpRequest get(String path) throws URISyntaxException
  {
    return HttpRequest.newBuilder(new URI(SERVER_URI + path)).GET().build();
  }

  HttpRequest delete(String path) throws URISyntaxException
  {
    return HttpRequest.newBuilder(new URI(SERVER_URI + path)).DELETE().build();
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

  @SuppressWarnings("unchecked")
  <T> T callService(HttpRequest req, int expectedCode, Class<T> responseClass)
    throws IOException, InterruptedException
  {
    HttpResponse<String> response = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
    String body = response.body();
    assertThat(response.statusCode()).as("status code for response " + body).isEqualTo(expectedCode);
    // noinspection unchecked
    return String.class == responseClass ? (T)body : GSON.fromJson(body, responseClass);
  }
}
