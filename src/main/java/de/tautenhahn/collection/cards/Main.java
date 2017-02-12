package de.tautenhahn.collection.cards;


import java.io.IOException;

import de.tautenhahn.collection.process.RestServer;

/**
 * Entry class for the card collecting application.
 * @author TT
 *
 */
public class Main
{

  /**
   * Starts the application from command line.
   * @param args to be changed
 * @throws IOException 
   */
  public static void main(String[] args) throws IOException
  {
	  CardApplicationContext.init();
	  RestServer.getInstance().start();
	  System.out.println("Server started, point your browser to http://loacalhost:4567/search/deck");
  }
}
