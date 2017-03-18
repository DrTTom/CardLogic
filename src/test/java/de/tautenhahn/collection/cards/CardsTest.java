package de.tautenhahn.collection.cards;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.DescribedObjectInterpreter;
import de.tautenhahn.collection.generic.data.Question;
import de.tautenhahn.collection.generic.data.TypeBasedEnumWithForeignKey;
import de.tautenhahn.collection.generic.data.TypeBasedEnumeration;
import de.tautenhahn.collection.generic.persistence.WorkspacePersistence;
import de.tautenhahn.collection.process.ProcessScheduler;
import de.tautenhahn.collection.process.SearchProcess;
import de.tautenhahn.collection.process.SearchResult;


/**
 * Base class for testing with card application context and test data.
 * 
 * @author TT
 */
public class CardsTest
{

  /**
   * Do not require Apache library to remove a directory tree.
   */
  private static final class DeleteDirTree extends SimpleFileVisitor<Path>
  {

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException
    {
      Files.delete(file);
      return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException
    {
      Files.delete(dir);
      return FileVisitResult.CONTINUE;
    }
  }

  private static DescribedObjectInterpreter deck;

  /**
   * Provides a context and some clean test data.
   * 
   * @throws IOException
   */
  @BeforeClass
  public static void setupStatic() throws IOException
  {
    CardApplicationContext.register("testingCards");
    try (InputStream ins = CardsTest.class.getResourceAsStream("/example.zip"))
    {
      ((WorkspacePersistence)ApplicationContext.getInstance().getPersistence()).importZip(ins);
    }
    deck = ApplicationContext.getInstance().getInterpreter("deck");
  }

  /**
   * Removes workspace content provided by {@link #setupStatic()}.
   * 
   * @throws IOException
   */
  @AfterClass
  public static void tearDownStatic() throws IOException
  {
    Path collectionBaseDir = Paths.get(System.getProperty("user.home"), ".Collection", "testingCards");
    Files.walkFileTree(collectionBaseDir, new DeleteDirTree());
  }

  /**
   * Assert that {@link TypeBasedEnumeration} can provide allowed values and translations.
   * 
   * @throws IOException
   */
  @Test
  public void typeBaseEnum() throws IOException
  {
    TypeBasedEnumeration systemUnderTest = (TypeBasedEnumeration)deck.getAttributeInterpreter("maker");
    assertThat("allowed values", systemUnderTest.getAllowedValues(null), hasItem("Alf Cooke"));
    assertThat("error code", systemUnderTest.check("wrong", null), is("msg.error.invalidOption"));
    assertThat("name", systemUnderTest.toName("Cooke"), is("Alf Cooke"));
    assertThat("key", systemUnderTest.toKey("Alf Cooke"), is("Cooke"));
  }

  /**
   * Assert that {@link TypeBasedEnumWithForeignKey} provides allowed values in accordance with the respective
   * context.
   * 
   * @throws IOException
   */
  @Test
  public void foreignKey() throws IOException
  {
    TypeBasedEnumWithForeignKey systemUnderTest = (TypeBasedEnumWithForeignKey)deck.getAttributeInterpreter("pattern");
    DescribedObject germanDeck = new DescribedObject("deck", "1");
    germanDeck.getAttributes().put("suits", "deutsch");
    DescribedObject frenchDeck = new DescribedObject("deck", "2");
    frenchDeck.getAttributes().put("suits", "französisch");

    assertThat("allowed values",
               systemUnderTest.getAllowedValues(germanDeck),
               allOf(hasItem("Hallisches Bild"), not(hasItem("Berliner Bild"))));
    assertThat("allowed values",
               systemUnderTest.getAllowedValues(frenchDeck),
               allOf(hasItem("Berliner Bild"), not(hasItem("Hallisches Bild"))));
    assertThat("error code",
               systemUnderTest.check("Berliner Bild", germanDeck),
               is("msg.error.optionMismatches.suits"));
  }

  /**
   * Asserts that search process filters results, adapts option values and reports error. Note that error
   * reporting is for information only, process still works even if search criteria contain errors.
   * 
   * @throws Exception
   */
  @Test
  public void search() throws Exception
  {
    SearchProcess systemUnderTest = ProcessScheduler.getInstance().getSearch("deck");
    SearchResult result = systemUnderTest.search(Collections.emptyMap());
    Question suitQuestion = getQuestion(result, "suits");
    assertThat(suitQuestion.getAllowedValues(), hasItem("deutsch"));
    int numberTotal = result.getNumberTotal();
    assertThat(result.getNumberPossible(), is(numberTotal));
    assertThat(getQuestion(result, "pattern").getAllowedValues(), hasItem("Berliner Bild"));

    result = systemUnderTest.search(Collections.singletonMap("suits", "deutsch"));
    assertThat(result.getNumberPossible(), lessThan(numberTotal));
    assertThat(getQuestion(result, "pattern").getAllowedValues(), not(hasItem("Berliner Bild")));
    assertThat(getQuestion(result, "suits").getProblem(), nullValue());
    assertThat(result.getTranslations().get("maker").get("Scharff"), is("Walter Scharff"));

    result = systemUnderTest.search(Collections.singletonMap("suits", "marsianisch"));
    assertThat(getQuestion(result, "suits").getProblem(), is("msg.error.invalidOption"));
  }

  private Question getQuestion(SearchResult result, String paramName) throws AssertionError
  {
    return result.getQuestions()
                 .stream()
                 .filter(q -> q.getParamName().equals(paramName))
                 .findAny()
                 .orElseThrow(() -> new AssertionError());
  }

}
