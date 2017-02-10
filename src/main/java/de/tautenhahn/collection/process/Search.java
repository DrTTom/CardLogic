package de.tautenhahn.collection.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.AttributeInterpreter;
import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.DescribedObjectInterpreter;
import de.tautenhahn.collection.generic.data.Question;


/**
 * Handles the search process. There is always exactly at most one search going on for each object type. The
 * search mask may be passed to the Submission process, primary key values from elements of the search result
 * may be passed to View, Edit or Delete processes.
 *
 * @author TT
 */
public class Search
{

  private DescribedObject searchMask;

  private final String type;

  private final DescribedObjectInterpreter interpreter;

  /**
   * Data for deciding whether to do a new search or a refinement.
   */
  private final Map<String, String> matchedAttributes = new HashMap<>();

  private boolean queryMeansRefine;

  private String queryText;

  private Search(String type)
  {
    this.type = type;
    interpreter = ApplicationContext.getInstance().getInterpreter(type);
  }

  /**
   * May be empty as long as initialization appears to be too expensive.
   */
  private final List<DescribedObject> matches = new ArrayList<>();

  private int numberTotal;

  private int numberMatch;

  public void clear()
  {
    matchedAttributes.clear();
    searchMask.getAttributes().clear();
    queryMeansRefine = false;
    queryText = null;
  }

  public void setAttribute(String name, String value)
  {
    AttributeInterpreter ai = interpreter.getAttributeInterpreter(name);
    if (!ai.isLegalValue(value))
    {
      throw new IllegalArgumentException(name); // TODO implement some message handling
    }
    searchMask.getAttributes().put(name, value);
  }

  public void setQueryText(String value, boolean refine)
  {
    queryText = value;
    queryMeansRefine = refine;
  }

  /**
   * Actually do the search with all the restrictions specified so far.
   */
  public void search()
  {
    // TODO: handle queryText

  }

  public List<Question> getQuestions()
  {
    return null;
  }
}
