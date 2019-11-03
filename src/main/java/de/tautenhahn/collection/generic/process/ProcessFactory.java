package de.tautenhahn.collection.generic.process;

import java.util.HashMap;
import java.util.Map;


/**
 * Provides process instances.
 *
 * @author TT
 */
public class ProcessFactory
{

  private static final ProcessFactory INSTANCE = new ProcessFactory();

  private final Map<String, SearchProcess> searches = new HashMap<>();

  /**
   * Singleton getter.
   */
  public static ProcessFactory getInstance()
  {
    return INSTANCE;
  }

  /**
   * Returns the search process for specified type.
   *
   * @param type
   */
  public SearchProcess getSearch(String type)
  {
    SearchProcess result = searches.get(type);
    if (result == null)
    {
      result = new SearchProcess(type);
      searches.put(type, result);
    }
    return result;
  }

  /**
   * Returns the view process.
   */
  public ViewProcess getView()
  {
    return new ViewProcess();
  }

  /**
   * Returns the submission process.
   */
  public SubmissionProcess getSubmission(String type)
  {
    return new SubmissionProcess(type);
  }

  /**
   * @return the deletion process.
   */
  public DeletionProcess getDelete()
  {
    return new DeletionProcess();
  }
}
