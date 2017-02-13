package de.tautenhahn.collection.process;

import java.util.HashMap;
import java.util.Map;


/**
 * Provides process instances.
 * 
 * @author jean
 */
public class ProcessScheduler
{

  private static final ProcessScheduler INSTANCE = new ProcessScheduler();

  private Map<String, SearchProcess> searches = new HashMap<>();

  public static ProcessScheduler getInstance()
  {
    return INSTANCE;
  }

  public SearchProcess getCurrentSearch(String type)
  {
    SearchProcess result = searches.get(type);
    if (result == null)
    {
      result = new SearchProcess(type);
      searches.put(type, result);
    }
    return result;
  }
}
