package de.tautenhahn.collection.cards;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.DescribedObjectInterpreter;
import de.tautenhahn.collection.generic.data.SubmissionResponse;
import de.tautenhahn.collection.generic.data.TypeBasedEnumWithForeignKey;
import de.tautenhahn.collection.generic.data.TypeBasedEnumeration;
import de.tautenhahn.collection.generic.data.question.ChoiceQuestion;
import de.tautenhahn.collection.generic.data.question.Question;
import de.tautenhahn.collection.generic.persistence.WorkspacePersistence;
import de.tautenhahn.collection.generic.process.ProcessFactory;
import de.tautenhahn.collection.generic.process.RestServer;
import de.tautenhahn.collection.generic.process.SearchProcess;
import de.tautenhahn.collection.generic.process.SearchResult;
import de.tautenhahn.collection.generic.process.SubmissionProcess;
import spark.Spark;


/**
 * Base class for testing with card application context and test data. Tests here may address generic
 * functionality which works only with some present data.
 *
 * @author TT
 */
public class CardsTest
{

  private static final Random MEASURE_SOURCE = new Random();

  public static final String PATTERN = "pattern";

  private static DescribedObjectInterpreter deck;

  private static ApplicationContext application;

  /**
   * Provides a context and some clean test data.
   *
   * @throws IOException
   */
  @BeforeAll
  public static void setupStatic() throws IOException
  {
    CardApplicationContext.register();
    application = ApplicationContext.getInstance();
    application.getPersistence().init("testingCards");
    try (InputStream ins = CardsTest.class.getResourceAsStream("/example.zip"))
    {
      ((WorkspacePersistence)application.getPersistence()).importZip(ins);
    }
    deck = application.getInterpreter("deck");
  }

  /**
   * Removes workspace content provided by {@link #setupStatic()}.
   *
   * @throws IOException
   */
  @AfterAll
  public static void tearDownStatic() throws IOException
  {
    Path collectionBaseDir = Paths.get(System.getProperty("user.home"), ".Collection", "testingCards");
    Files.walk(collectionBaseDir).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
  }

  /**
   * Assert that {@link TypeBasedEnumeration} can provide allowed values and translations.
   */
  @Test
  public void typeBaseEnum()
  {
    TypeBasedEnumeration systemUnderTest = (TypeBasedEnumeration)deck.getAttributeInterpreter("maker");
    assertThat(systemUnderTest.getAllowedValues(null)).as("allowed values").contains("Cooke");
    assertThat(systemUnderTest.check("wrong", null)).as("error code").isEqualTo("msg.error.invalidOption");
    assertThat(systemUnderTest.toDisplayValue("Cooke")).isEqualTo("Alf Cooke");
  }

  /**
   * Assert that {@link TypeBasedEnumWithForeignKey} provides allowed values in accordance with the respective
   * context.
   */
  @Test
  public void foreignKey()
  {
    TypeBasedEnumWithForeignKey systemUnderTest = (TypeBasedEnumWithForeignKey)deck.getAttributeInterpreter(PATTERN);
    DescribedObject germanDeck = new DescribedObject("deck", "1");
    germanDeck.getAttributes().put("suits", "deutsch");
    DescribedObject frenchDeck = new DescribedObject("deck", "2");
    frenchDeck.getAttributes().put("suits", "französisch");

    assertThat(systemUnderTest.getAllowedValues(germanDeck)).as("allowed values")
                                                            .contains("halle")
                                                            .doesNotContain("berlin");
    assertThat(systemUnderTest.getAllowedValues(frenchDeck)).as("allowed values")
                                                            .contains("berlin")
                                                            .doesNotContain("halle");
    assertThat(systemUnderTest.check("berlin", germanDeck)).isEqualTo("msg.error.optionMismatches.suits");
  }

  /**
   * Assert that maker sign choice is update with maker.
   */
  @Test
  public void imageChoice()
  {
    TypeBasedEnumWithForeignKey systemUnderTest = (TypeBasedEnumWithForeignKey)deck.getAttributeInterpreter("makerSign");
    DescribedObject myDeck = new DescribedObject("deck", "1");
    assertThat(systemUnderTest.getAllowedValues(myDeck)).hasSize(37);
    myDeck.getAttributes().put("maker", "AS");
    assertThat(systemUnderTest.getAllowedValues(myDeck)).hasSize(7);
    // TODO: check that question describes images
  }

  /**
   * Asserts that search process filters results, adapts option values and reports errors. Note that error
   * reporting is for information only, process still works even if search criteria contain errors.
   */
  @Test
  public void search()
  {
    SearchProcess systemUnderTest = ProcessFactory.getInstance().getSearch("deck");
    SearchResult result = systemUnderTest.search(Collections.emptyMap());
    ChoiceQuestion suitQuestion = (ChoiceQuestion)getQuestion(result.getQuestions(), "suits");
    assertThat(suitQuestion.getOptions().values()).contains("deutsch");
    int numberTotal = result.getNumberTotal();
    assertThat(result.getNumberPossible()).isEqualTo(numberTotal);
    ChoiceQuestion patternQuestion = (ChoiceQuestion)getQuestion(result.getQuestions(), PATTERN);
    assertThat(patternQuestion.getOptions().values()).contains("Berliner Bild");

    result = systemUnderTest.search(Collections.singletonMap("suits", "deutsch"));
    assertThat(result.getNumberPossible()).isLessThan(numberTotal);
    patternQuestion = (ChoiceQuestion)getQuestion(result.getQuestions(), PATTERN);
    assertThat(patternQuestion.getOptions().values()).doesNotContain("Berliner Bild");
    assertThat(getQuestion(result.getQuestions(), "suits").getProblem()).isNull();

    HashMap<String, String> params = new HashMap<>();
    params.put("suits", "marsianisch");
    params.put(PATTERN, "french");

    result = systemUnderTest.search(params);
    assertThat(getQuestion(result.getQuestions(),
                           "suits").getProblem()).isEqualTo(application.getText("msg.error.invalidOption"));
    Question pq = getQuestion(result.getQuestions(), PATTERN);
    assertThat(pq.getProblem()).isEqualTo(application.getText("msg.error.optionMismatches.suits"));
    assertThat(pq.getValue()).isEqualTo("french");
  }

  /**
   * Asserts that a {@link TypeBasedEnumWithForeignKey} question returns correct error message and contains
   * the "keep" and "delete" options.
   */
  @Test
  public void foreignKeyViolated()
  {
    DescribedObject myDeck = new DescribedObject("deck", null);
    myDeck.getAttributes().put("suits", "deutsch");
    myDeck.getAttributes().put(PATTERN, "french");
    DescribedObjectInterpreter interpreter = application.getInterpreter(myDeck.getType());
    ChoiceQuestion pq = (ChoiceQuestion)getQuestion(interpreter.getQuestions(myDeck, false), PATTERN);
    assertThat(pq.getProblem()).isEqualTo(application.getText("msg.error.optionMismatches.suits"));
    assertThat(pq.getValue()).isEqualTo("french");
    assertThat(pq.getOptions().values()).contains("Französisches Bild");
    assertThat(pq.getOptions().values()).contains("(Keine Angabe)");
  }

  /**
   * Asserts that submission process creates new object which is returned with next search. Furthermore, make
   * sure translated attributes are filled correctly.
   */
  @Test
  public void submitPositive()
  {

    DescribedObject newDeck = createValidDeck();

    SubmissionResponse result = doSubmit(newDeck, false, true);
    assertThat(result.getPrimaryKey()).as("created primary key").isNotNull();
    DescribedObject stored = application.getPersistence().find("deck", result.getPrimaryKey());
    assertThat(stored.getAttributes().get(PATTERN)).isEqualTo("halle");
  }

  /**
   * Asserts that submission process aborts with error message in data is inconsistent.
   */
  @Test
  public void submitInconsistentData()
  {
    DescribedObject newDeck = createValidDeck();
    newDeck.getAttributes().put("printedLatest", "2017");
    SubmissionResponse result = doSubmit(newDeck, false, false);
    Stream<Question> boughtQuestions = result.getQuestions()
                                             .stream()
                                             .filter(q -> "bought".equals(q.getParamName()));
    assertThat(boughtQuestions).extracting(Question::getProblem)
                               .containsExactly("Das Jahr darf nicht vor \"gedruckt spätestens\" liegen.");
  }

  /**
   * Asserts that submission process stores inconsistent data if forced.
   */
  @Test
  public void submitForce()
  {
    DescribedObject newDeck = createValidDeck();
    newDeck.getAttributes().put("printedEarliest", "2017");
    newDeck.getAttributes().remove("bought");
    SubmissionResponse result = doSubmit(newDeck, true, true);
    assertThat(result.getPrimaryKey()).isNotNull();
  }

  /**
   * Asserts that basic REST API provides something at most important URLs.
   *
   * @throws Exception
   */
  @Test
  public void callREST() throws Exception
  {
    try
    {
      RestServer.getInstance().start();
      Spark.awaitInitialization();
      try (
        InputStream ins = (InputStream)new URL("http://localhost:4567/collected/maker/search").getContent())
      {
        assertThat(ins.available()).isGreaterThan(255);
      }
      assertThat(new URL("http://localhost:4567/download/deck/e0/77.jpg").getContent()).isInstanceOf(ImageProducer.class);
    }
    finally
    {
      RestServer.getInstance().stop();
    }
  }

  private SubmissionResponse doSubmit(DescribedObject newDeck, boolean force, boolean expectSuccess)
  {
    SubmissionProcess systemUnderTest = ProcessFactory.getInstance().getSubmission("deck");
    SearchProcess search = ProcessFactory.getInstance().getSearch("deck");
    SearchResult before = search.search(newDeck.getAttributes());
    assertThat(before.getNumberMatching()).isEqualTo(0);

    SubmissionResponse result = systemUnderTest.submit(newDeck.getAttributes(), force);
    // assertThat("done (" + result.getStatus() + ")", result.isDone(), is(expectSuccess));

    SearchResult after = search.search(newDeck.getAttributes());
    assertThat(after.getNumberMatching()).isEqualTo(expectSuccess ? 1 : 0);
    assertThat(after.getNumberTotal()).isEqualTo(before.getNumberTotal() + (expectSuccess ? 1 : 0));
    return result;
  }

  private DescribedObject createValidDeck()
  {
    DescribedObject newDeck = new DescribedObject("deck", null);
    newDeck.getAttributes().put("suits", "deutsch");
    newDeck.getAttributes().put(PATTERN, "halle");
    newDeck.getAttributes()
           .put("format", (10 + MEASURE_SOURCE.nextInt(100)) + "x" + (50 + MEASURE_SOURCE.nextInt(200)));
    newDeck.getAttributes().put("specialMeasure", "12x" + (10 + MEASURE_SOURCE.nextInt(40)));
    newDeck.getAttributes().put("index", "7-10UOK");
    newDeck.getAttributes().put("numIndex", "4");
    newDeck.getAttributes().put("numberCards", "32");
    newDeck.getAttributes().put("name", "TestDeck#" + UUID.randomUUID());
    newDeck.getAttributes().put("condition", "ungespielt");
    newDeck.getAttributes().put("bought", "1998");
    newDeck.getAttributes().put("location", "Vitrine");
    return newDeck;
  }

  private Question getQuestion(List<Question> questions, String paramName) throws AssertionError
  {
    return questions.stream()
                    .filter(q -> q.getParamName().equals(paramName))
                    .findAny()
                    .orElseThrow(AssertionError::new);
  }
}
