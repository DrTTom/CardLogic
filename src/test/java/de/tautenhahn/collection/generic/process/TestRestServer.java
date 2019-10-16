package de.tautenhahn.collection.generic.process;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import com.google.gson.Gson;
import de.tautenhahn.collection.cards.CardApplicationContext;
import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.DescribedObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import spark.Spark;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

/**
 * Tests for calling the REST paths on the server.
 *
 * @author TT
 */
@Slf4j
public class TestRestServer
{
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
        String index = callService(get(""), 200);
        assertThat(index).startsWith("<!DOCTYPE html>");
    }

    /**
     * Create, search, update, check and delete the most simple entity in the system.
     * @throws Exception all the unexpected Exceptions go to test protocol
     */
    @Test
    void crudSimpleType() throws Exception
    {
        Map<String, String> attribs = Map.of("fullName", "Ostermann AG", "remark", "test data");
        DescribedObject data = new DescribedObject("maker", "OAG", attribs);
        String x = callService(post("/collected/maker", data), 200);
    }

    HttpRequest get(String path) throws URISyntaxException
    {
        return HttpRequest.newBuilder(new URI(SERVER_URI + path)).GET().build();
    }

    HttpRequest post(String path, Object value) throws URISyntaxException
    {
        String content = new Gson().toJson(value);
        return HttpRequest.newBuilder(new URI(SERVER_URI + path)).POST(HttpRequest.BodyPublishers.ofString(content)).build();
    }

    String callService(HttpRequest req, int expectedCode) throws IOException, InterruptedException
    {
        HttpResponse<String> response = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
        assertThat(response.statusCode()).as("status code").isEqualTo(expectedCode);
        return response.body();
    }
}
