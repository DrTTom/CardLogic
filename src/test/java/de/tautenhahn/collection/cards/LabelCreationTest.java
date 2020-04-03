package de.tautenhahn.collection.cards;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import de.tautenhahn.collection.cards.labels.DeckLabelCreator;
import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.persistence.Persistence;
import de.tautenhahn.collection.generic.persistence.WorkspacePersistence;
import de.tautenhahn.collection.generic.renderer.DataRenderer;
import de.tautenhahn.collection.generic.renderer.DocxLabelRenderer;
import de.tautenhahn.collection.generic.renderer.Label;


/**
 * Uses the label creator to build some output.
 * 
 * @author ttautenhahn
 */
public class LabelCreationTest
{

  static Persistence persistence;

  /**
   * Provides a context and some clean test data.
   *
   * @throws IOException
   */
  @BeforeAll
  public static void setupStatic() throws IOException
  {
    CardApplicationContext.register();
    persistence = ApplicationContext.getInstance().getPersistence();
    persistence.init("testingCards");
    try (InputStream ins = CardsTest.class.getResourceAsStream("/example.zip"))
    {
      ((WorkspacePersistence)persistence).importZip(ins);
    }
  }

  @Test
  void createLabels() throws Exception
  {
    DeckLabelCreator creator = new DeckLabelCreator();
    DataRenderer<Label> renderer = new DocxLabelRenderer();
    List<Label> labels = persistence.findAll("deck").map(creator::createLabel).collect(Collectors.toList());
    try (OutputStream target = new FileOutputStream("build/labels.docx"))
    {
      renderer.render(labels, target);
    }
  }
}
