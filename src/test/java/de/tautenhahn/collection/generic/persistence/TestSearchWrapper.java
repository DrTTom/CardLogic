package de.tautenhahn.collection.generic.persistence;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.tautenhahn.collection.cards.CardApplicationContext;
import de.tautenhahn.collection.generic.data.DescribedObject;


/**
 * Unit tests for searching.
 *
 * @author TT
 */
public class TestSearchWrapper
{

  private final Path directory = Paths.get(System.getProperty("java.io.tmpdir"), "collectionSearchTest");

  private SearchWrapper systemUnderTest;

  /**
   * Provides a search wrapper and registers an application context.
   *
   * @throws IOException
   */
  @Before
  public void setUp() throws IOException
  {
    CardApplicationContext.register();
    systemUnderTest = new SearchWrapper(directory);
  }

  /**
   * Deletes the search index created by the test.
   *
   * @throws IOException
   */
  @After
  public void tearDown() throws IOException
  {
    Files.list(directory).forEach(p -> {
      try
      {
        Files.delete(p);
      }
      catch (IOException e)
      {
        throw new RuntimeException(e);
      }
    });
    Files.delete(directory);
  }

  /**
   * Assert that an object can be found by any supported query type (which we restrict at will :o)).
   *
   * @throws IOException
   */
  @Test
  public void search() throws IOException
  {

    DescribedObject deck1 = new DescribedObject("deck", "1");
    DescribedObject deck2 = new DescribedObject("deck", "2");
    deck1.getAttributes().put("remark", "This is a text to find something in");
    deck2.getAttributes().put("remark", "Another text with some other content");
    systemUnderTest.addToIndex(deck1, deck2);
    assertThat(systemUnderTest.search("something", "remark"), contains("1"));
    assertThat(systemUnderTest.search("some*", "remark"), contains("1", "2"));
    assertThat(systemUnderTest.search("*other", "remark"), contains("2"));
    assertThat(systemUnderTest.search("??other", "remark"), contains("2"));
    assertThat(systemUnderTest.search("?other", "remark"), empty());
  }
}
