package de.tautenhahn.collection.cards.deck;

import java.util.LinkedHashMap;
import java.util.Map;

import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.TypeBasedEnumWithForeignKey;
import de.tautenhahn.collection.generic.data.question.ImageChoiceQuestion;
import de.tautenhahn.collection.generic.data.question.Question;


/**
 * Defines the makers logo on the playing cards.
 *
 * @author TT
 */
public class MakerSign extends TypeBasedEnumWithForeignKey
{

  /**
   * Creates immutable instance.
   */
  public MakerSign()
  {
    super("makerSign", "maker", 50, Flag.OPTIONAL, Flag.EXACT);
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
    System.out.println(result.getAllowedValues());
    System.out.println(urlByValue);
    result.setUrls(urlByValue);
    result.setFormat("auto", "90");
    return result;
  }

}
