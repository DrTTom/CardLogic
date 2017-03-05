package de.tautenhahn.collection.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.AttributeInterpreter;
import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.DescribedObjectInterpreter;
import de.tautenhahn.collection.generic.data.Message;
import de.tautenhahn.collection.generic.data.Similarity;


/**
 * Handles the search process. There is always exactly at most one search going on for each object type. The
 * search mask may be passed to the Submission process, primary key values from elements of the search result
 * may be passed to View, Edit or Delete processes.
 *
 * @author TT
 */
public class SearchProcess
{

  private final DescribedObject searchMask;

  private final String type;

  private final DescribedObjectInterpreter interpreter;

  /**
   * Data for deciding whether to do a new search or a refinement.
   */
  private final Map<String, String> matchedAttributes = new HashMap<>();

  private boolean queryMeansRefine;

  private String queryText;

  SearchProcess(String type)
  {
    this.type = type;
    searchMask = new DescribedObject(type, null);
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

  public Message setAttribute(String name, String value)
  {
    AttributeInterpreter ai = interpreter.getAttributeInterpreter(name);
    if (ai == null)
    {
      return new Message();
    }
    if (ai.checkValue(value, searchMask) != null)
    {
      return new Message(); // TODO implement some message handling
    }
    searchMask.getAttributes().put(name, value);
    return null;
  }

  public void setQueryText(String value, boolean refine)
  {
    queryText = value;
    queryMeansRefine = refine;
  }

  /**
   * Actually do the search with all the restrictions specified so far.
   */
  public Search execute()
  {
    Search result = new Search();
    result.setType(type);
    result.setNumberTotal(ApplicationContext.getInstance().getPersistence().getNumberItems(type));
    result.setQueryText(queryText);
    result.setQuestions(new ArrayList<>(interpreter.getQuestions(searchMask)));
    result.getQuestions().removeIf(q -> "illustrate".equals(q.getForm()));

    // TODO: add caching, use exact attributes, sort, ...
    Map<DescribedObject, Similarity> candidates = new LinkedHashMap<>();
    ApplicationContext.getInstance().getPersistence().findAll(type).forEach(d -> {
      Similarity sim = interpreter.countSimilarity(searchMask, d);
      if (sim.possiblyEqual())
      {
        candidates.put(d, sim);
      }
    });

    result.setNumberPossible(candidates.size());
    result.setNumberMatching((int)candidates.values().stream().filter(x -> x.probablyEqual()).count());
    if (result.getNumberMatching() > 0 || candidates.size() < 100)
    {
      result.setMatches(new ArrayList<>(candidates.keySet()));
      result.getMatches().sort((a, b) -> candidates.get(b).compareTo(candidates.get(a)));
    }
    clear();

    return result;

  }


  public void setQueryText(String queryText)
  {
    this.queryText = queryText;
  }


  public DescribedObject getSearchMask()
  {
    return searchMask;
  }

  public String getType()
  {
    return type;
  }

  public List<DescribedObject> getMatches()
  {
    return matches;
  }

  public int getNumberTotal()
  {
    return numberTotal;
  }

  public int getNumberMatch()
  {
    return numberMatch;
  }
}
