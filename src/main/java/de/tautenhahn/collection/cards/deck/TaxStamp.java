package de.tautenhahn.collection.cards.deck;

import java.util.LinkedHashMap;
import java.util.Map;

import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.TypeBasedEnumeration;
import de.tautenhahn.collection.generic.data.question.ImageChoiceQuestion;
import de.tautenhahn.collection.generic.data.question.Question;


/**
 * Defines the tax stamp (stamps only, no tax aces or wrappers) on a deck.
 *
 * @author TT
 */
public class TaxStamp extends TypeBasedEnumeration
{

  /**
   * Creates immutable instance.
   */
  public TaxStamp()
  {
    super("taxStamp", 50, Flag.OPTIONAL);
  }

  @Override
  public Question getQuestion(DescribedObject object)
  {
    ImageChoiceQuestion result = createQuestion(object,
                                                (text, group) -> new ImageChoiceQuestion(getName(), text,
                                                                                         group));
    result.setAllowedValues(getAllowedValues(object));
    Map<String, String> urlByValue = new LinkedHashMap<>();
    urlByValue.put("Kein", "");
    result.getAllowedValues().stream().sorted().forEach(v -> urlByValue.put(v, imageByName.get(v)));
    System.out.println(urlByValue);
    result.setUrls(urlByValue);
    result.setFormat("auto", "120");
    return result;
  }
}
