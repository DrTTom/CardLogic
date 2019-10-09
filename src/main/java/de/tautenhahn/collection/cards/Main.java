package de.tautenhahn.collection.cards;

import java.io.IOException;
import java.io.PrintStream;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.process.RestServer;


/**
 * Entry class for the card collecting application.
 *
 * @author TT
 */
public final class Main
{

  private static final PrintStream OUT = System.out;

  private Main()
  {
    // no instances
  }

  /**
   * Starts the application from command line.
   *
   * @param args to be changed
   * @throws IOException
   */
  public static void main(String[] args) throws IOException
  {
    CardApplicationContext.register();
    ApplicationContext.getInstance().getPersistence().init("cards");
    RestServer.getInstance().start();
    try
    {
      Runtime.getRuntime().exec("firefox http://localhost:4567/index.html");
    }
    catch (RuntimeException e)
    {
      OUT.println("Server started, point your browser to http://localhost:4567/index.html");
    }
  }
}
