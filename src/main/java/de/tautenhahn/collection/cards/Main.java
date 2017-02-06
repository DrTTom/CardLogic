package de.tautenhahn.collection.cards;


import static spark.Spark.get;


public class Main
{

  /**
   * Port ist
   * 
   * @param args
   */
  public static void main(String[] args)
  {
    get("/hello/:name", (req, res) -> {
      res.type("text/plain");
      String name = req.params(":name");
      return "Hello " + (name == null ? "World" : name);
    });
    get("/other/:name", (req, res) -> {
      res.type("text/plain");
      String name = req.params(":name");
      return "Other " + (name == null ? "World" : name);
    });
    System.out.println("End reached");
  }
}
