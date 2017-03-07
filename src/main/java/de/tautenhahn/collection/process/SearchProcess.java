package de.tautenhahn.collection.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.AttributeInterpreter;
import de.tautenhahn.collection.generic.data.AttributeTranslator;
import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.DescribedObjectInterpreter;
import de.tautenhahn.collection.generic.data.Similarity;
import de.tautenhahn.collection.generic.persistence.Persistence;


/**
 * Handles the search process. The process may buffer results of earlier search for better performance but has
 * no further state. Objects in search result must be enriched by translations of attribute values if
 * available. An object in the search result may be passed to a view which may load auxiliary objects for
 * further details. The submission and edit processes may re-use questions created and answered by this
 * search.
 *
 * @author TT
 */
public class SearchProcess
{

  private static final Persistence PERSISTENCE = ApplicationContext.getInstance().getPersistence();

  /**
   * Caching only one last search, optimization for multi-user handling not done.
   */
  private Map<String, String> lastSearch;

  private final String type;

  private final DescribedObjectInterpreter interpreter;

  SearchProcess(String type)
  {
    this.type = type;
    interpreter = ApplicationContext.getInstance().getInterpreter(type);
  }

  /**
   * May be empty as long as initialization appears to be too expensive.
   */
  private List<DescribedObject> lastCandidates;

  /**
   * Do the search. If too expensive, maybe cache old similarity values as well. However, current
   * implementation does not require the similarity to be additive.
   */
  public Search execute(Map<String, String> parameters)
  {
    DescribedObject searchMask = translateInput(parameters);

    Stream<DescribedObject> candidates;
    synchronized (this)
    {
      if (lastCandidates != null && refinesLastSearch(parameters))
      {
        candidates = lastCandidates.stream();
      }
      else
      {
        candidates = PERSISTENCE.findAll(type);
      }
    }

    Search result = createSearchQuestions(searchMask);
    computeSearchResults(result, candidates, searchMask);

    return result;
  }

  private DescribedObject translateInput(Map<String, String> parameters)
  {
    Map<String, String> attribs = new HashMap<>();

    for ( String key : interpreter.getSupportedAttributes() )
    {
      String value = parameters.get(key);
      if (value == null)
      {
        continue;
      }
      AttributeInterpreter ai = interpreter.getAttributeInterpreter(key);
      attribs.put(key, ai instanceof AttributeTranslator ? ((AttributeTranslator)ai).toKey(value) : value);
    }

    return new DescribedObject(type, null, attribs);
  }

  private void computeSearchResults(Search result,
                                    Stream<DescribedObject> candidates,
                                    DescribedObject searchMask)
  {
    Map<DescribedObject, Similarity> similars = new LinkedHashMap<>();
    List<DescribedObject> remainingCandidates = new ArrayList<>();
    candidates.forEach(d -> {
      Similarity sim = interpreter.countSimilarity(searchMask, d);
      if (sim.possiblyEqual())
      {
        remainingCandidates.add(d);
        similars.put(d, sim);
      }
    });

    result.setNumberPossible(similars.size());
    result.setNumberMatching((int)similars.values().stream().filter(x -> x.probablyEqual()).count());
    if (result.getNumberMatching() > 0 || similars.size() < 100)
    {
      result.setMatches(new ArrayList<>(similars.keySet()));
      result.getMatches().sort((a, b) -> similars.get(b).compareTo(similars.get(a)));
    }

    synchronized (this)
    {
      lastSearch = searchMask.getAttributes();
      lastCandidates = remainingCandidates;
    }
  }

  private Search createSearchQuestions(DescribedObject questionContext)
  {
    Search result = new Search();
    result.setType(type);
    result.setNumberTotal(PERSISTENCE.getNumberItems(type));
    result.setQuestions(new ArrayList<>(interpreter.getQuestions(questionContext)));
    result.getQuestions().removeIf(q -> "illustrate".equals(q.getForm()));

    return result;
  }

  private boolean refinesLastSearch(Map<String, String> params)
  {
    return lastSearch.entrySet().stream().allMatch(e -> e.getValue().equals(params.get(e.getKey())));
  }

}
