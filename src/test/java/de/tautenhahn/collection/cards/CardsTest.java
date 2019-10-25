package de.tautenhahn.collection.cards;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.image.ImageProducer;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Base class for testing with card application context and test data. Tests here may address generic functionality
 * which works only with some present data.
 *
 * @author TT
 */
public class CardsTest
{

    private static final Random MEASURE_SOURCE = new Random();

    private static DescribedObjectInterpreter deck;

    private static ApplicationContext application;

    /**
     * Provides a context and some clean test data.
     *
     * @throws IOException
     */
    @BeforeClass
    public static void setupStatic() throws IOException
    {
        CardApplicationContext.register();
        application = ApplicationContext.getInstance();
        application.getPersistence().init("testingCards");
        try (InputStream ins = CardsTest.class.getResourceAsStream("/example.zip"))
        {
            ((WorkspacePersistence) application.getPersistence()).importZip(ins);
        }
        deck = application.getInterpreter("deck");
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
        TypeBasedEnumeration systemUnderTest = (TypeBasedEnumeration) deck.getAttributeInterpreter("maker");
        assertThat("allowed values", systemUnderTest.getAllowedValues(null), hasItem("Alf Cooke"));
        assertThat("error code", systemUnderTest.check("wrong", null), is("msg.error.invalidOption"));
        assertThat("name", systemUnderTest.toDisplayValue("Cooke"), is("Alf Cooke"));
        assertThat("key", systemUnderTest.toInternalValue("Alf Cooke"), is("Cooke"));
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
        TypeBasedEnumWithForeignKey systemUnderTest =
            (TypeBasedEnumWithForeignKey) deck.getAttributeInterpreter("pattern");
        DescribedObject germanDeck = new DescribedObject("deck", "1");
        germanDeck.getAttributes().put("suits", "deutsch");
        DescribedObject frenchDeck = new DescribedObject("deck", "2");
        frenchDeck.getAttributes().put("suits", "französisch");

        assertThat("allowed values", systemUnderTest.getAllowedValues(germanDeck),
            allOf(hasItem("Hallisches Bild"), not(hasItem("Berliner Bild"))));
        assertThat("allowed values", systemUnderTest.getAllowedValues(frenchDeck),
            allOf(hasItem("Berliner Bild"), not(hasItem("Hallisches Bild"))));
        assertThat("error code", systemUnderTest.check("berlin", germanDeck), is("msg.error.optionMismatches.suits"));
    }

    /**
     * Assert that maker sign choice is update with maker.
     *
     * @throws IOException
     */
    @Test
    public void imageChoice() throws IOException
    {
        TypeBasedEnumWithForeignKey systemUnderTest =
            (TypeBasedEnumWithForeignKey) deck.getAttributeInterpreter("makerSign");
        DescribedObject myDeck = new DescribedObject("deck", "1");
        assertThat(systemUnderTest.getAllowedValues(myDeck), hasSize(37));
        myDeck.getAttributes().put("maker", "AS");
        assertThat(systemUnderTest.getAllowedValues(myDeck), hasSize(7));
        // TODO: check that question describes images
    }

    /**
     * Asserts that search process filters results, adapts option values and reports errors. Note that error reporting
     * is for information only, process still works even if search criteria contain errors.
     *
     * @throws Exception
     */
    @SuppressWarnings("boxing")
    @Test
    public void search() throws Exception
    {
        SearchProcess systemUnderTest = ProcessFactory.getInstance().getSearch("deck");
        SearchResult result = systemUnderTest.search(Collections.emptyMap());
        ChoiceQuestion suitQuestion = (ChoiceQuestion) getQuestion(result.getQuestions(), "suits");
        assertThat(suitQuestion.getOptions(), hasItem("deutsch"));
        int numberTotal = result.getNumberTotal();
        assertThat(result.getNumberPossible(), is(numberTotal));
        ChoiceQuestion patternQuestion = (ChoiceQuestion) getQuestion(result.getQuestions(), "pattern");
        assertThat(patternQuestion.getOptions(), hasItem("Berliner Bild"));

        result = systemUnderTest.search(Collections.singletonMap("suits", "deutsch"));
        assertThat(result.getNumberPossible(), lessThan(numberTotal));
        patternQuestion = (ChoiceQuestion) getQuestion(result.getQuestions(), "pattern");
        assertThat(patternQuestion.getOptions(), not(hasItem("Berliner Bild")));
        assertThat(getQuestion(result.getQuestions(), "suits").getProblem(), nullValue());
        assertThat(result.getTranslations().get("maker").get("Scharff"), is("Walter Scharff"));

        HashMap<String, String> params = new HashMap<>();
        params.put("suits", "marsianisch");
        params.put("pattern", "Französisches Bild");

        result = systemUnderTest.search(params);
        assertThat(getQuestion(result.getQuestions(), "suits").getProblem(),
            is(application.getText("msg.error.invalidOption")));
        Question pq = getQuestion(result.getQuestions(), "pattern");
        assertThat(pq.getProblem(), is(application.getText("msg.error.optionMismatches.suits")));
        assertThat(pq.getValue(), is("Französisches Bild"));
    }

    /**
     * Asserts that a {@link TypeBasedEnumWithForeignKey} question returns correct error message and contains the "keep"
     * and "delete" options.
     */
    @Test
    public void foreignKeyViolated()
    {
        DescribedObject myDeck = new DescribedObject("deck", null);
        myDeck.getAttributes().put("suits", "deutsch");
        myDeck.getAttributes().put("pattern", "french");
        DescribedObjectInterpreter interpreter = application.getInterpreter(myDeck.getType());
        ChoiceQuestion pq = (ChoiceQuestion) getQuestion(interpreter.getQuestions(myDeck, false), "pattern");
        assertThat(pq.getProblem(), is(application.getText("msg.error.optionMismatches.suits")));
        assertThat(pq.getValue(), is("Französisches Bild"));
        assertThat(pq.getOptions(), hasItem("Französisches Bild"));
        assertThat(pq.getOptions(), hasItem(""));
    }

    /**
     * Asserts that submission process creates new object which is returned with next search. Furthermore, make sure
     * translated attributes are filled correctly.
     */
    @Test
    public void submitPositive() throws Exception
    {

        DescribedObject newDeck = createValidDeck();

        SubmissionResponse result = doSubmit(newDeck, false, true);
        assertThat("created primary key", result.getPrimaryKey(), not(nullValue()));
        DescribedObject stored = application.getPersistence().find("deck", result.getPrimaryKey());
        assertThat(stored.getAttributes().get("pattern"), is("halle"));
    }

    /**
     * Asserts that submission process aborts with error message in data is inconsistent.
     */
    @Test
    public void submitInconsistentData() throws Exception
    {
        DescribedObject newDeck = createValidDeck();
        newDeck.getAttributes().put("printedLatest", "2017");
        SubmissionResponse result = doSubmit(newDeck, false, false);
        assertThat("created primary key", result.getPrimaryKey(), nullValue());
    }

    /**
     * Asserts that submission process stores inconsistent data if forced.
     */
    @Test
    public void submitForce() throws Exception
    {
        DescribedObject newDeck = createValidDeck();
        newDeck.getAttributes().put("printedEarliest", "2017");
        newDeck.getAttributes().remove("bought");
        SubmissionResponse result = doSubmit(newDeck, true, true);
        assertThat("created primary key", result.getPrimaryKey(), not(nullValue()));
    }

    /**
     * Asserts that basic REST API provides something at most important URLs.
     *
     * @throws Exception
     */
    @SuppressWarnings("boxing")
    @Test
    public void callREST() throws Exception
    {
        try
        {
            RestServer.getInstance().start();
            Thread.sleep(500);
            try (InputStream ins = (InputStream) new URL("http://localhost:4567/search/maker").getContent())
            {
                assertThat(ins.available(), greaterThan(255));
            }
            assertThat(new URL("http://localhost:4567/download/deck/e0/77.jpg").getContent(),
                instanceOf(ImageProducer.class));
        } finally
        {
            RestServer.getInstance().stop();
        }
    }

    @SuppressWarnings("boxing")
    private SubmissionResponse doSubmit(DescribedObject newDeck, boolean force, boolean expectSuccess)
    {
        SubmissionProcess systemUnderTest = ProcessFactory.getInstance().getSubmission("deck");
        SearchProcess search = ProcessFactory.getInstance().getSearch("deck");
        SearchResult before = search.search(newDeck.getAttributes());
        assertThat(before.getNumberMatching(), is(0));

        SubmissionResponse result = systemUnderTest.submit(newDeck.getAttributes(), force);
        // assertThat("done (" + result.getStatus() + ")", result.isDone(), is(expectSuccess));

        SearchResult after = search.search(newDeck.getAttributes());
        assertThat(after.getNumberMatching(), is(expectSuccess ? 1 : 0));
        assertThat(after.getNumberTotal(), is(before.getNumberTotal() + (expectSuccess ? 1 : 0)));
        return result;
    }

    private DescribedObject createValidDeck()
    {
        DescribedObject newDeck = new DescribedObject("deck", null);
        newDeck.getAttributes().put("suits", "deutsch");
        newDeck.getAttributes().put("pattern", "halle");
        newDeck
            .getAttributes()
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
        return questions
            .stream()
            .filter(q -> q.getParamName().equals(paramName))
            .findAny()
            .orElseThrow(() -> new AssertionError());
    }

    /**
     * Do not require Apache library to remove a directory tree.
     */
    static final class DeleteDirTree extends SimpleFileVisitor<Path>
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
}
