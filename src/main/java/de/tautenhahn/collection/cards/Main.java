package de.tautenhahn.collection.cards;


import java.io.IOException;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.process.RestServer;


/**
 * Entry class for the card collecting application.
 *
 * @author TT
 */
public class Main
{

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
    catch (Throwable t)
    {
      System.out.println("Server started, point your browser to http://localhost:4567/index.html");
    }
  }
}
