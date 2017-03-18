package de.tautenhahn.collection.cards;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.persistence.WorkspacePersistence;


/**
 * Base class for testing with card application context and test data.
 * 
 * @author TT
 */
public class CardsTest
{

  @BeforeClass
  public static void setupStatic() throws IOException
  {
    CardApplicationContext.register("testingCards");
    try (InputStream ins = CardsTest.class.getResourceAsStream("/example.zip"))
    {
      ((WorkspacePersistence)ApplicationContext.getInstance().getPersistence()).importZip(ins);
    }
  }

  @AfterClass
  public static void tearDownStatic() throws IOException
  {
    Path collectionBaseDir = Paths.get(System.getProperty("user.home"), ".Collection", "testingCards");
    Files.walkFileTree(collectionBaseDir, new SimpleFileVisitor<Path>()
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
    });
  }

  @Test
  public void test() throws IOException
  {
    DescribedObject item = new DescribedObject("egal", "fsfdsf");
    ApplicationContext.getInstance().getPersistence().store(item);
    ApplicationContext.getInstance().getPersistence().close();
  }

}
