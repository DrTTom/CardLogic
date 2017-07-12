package de.tautenhahn.collection.generic.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.question.Question;
import de.tautenhahn.collection.generic.data.question.TextChoiceQuestion;


/**
 * Enumeration with fixed set of allowed String values. Values of other types require a
 * {@link TypeBasedEnumeration}.
 *
 * @author TT
 */
public abstract class FixedEnumeration extends Enumeration
{

  /**
   * Returns array of translated values based on given message keys.
   *
   * @param paramName
   * @param keys
   */
  protected static String[] resolveKeys(String paramName, String... keys)
  {
    String[] result = new String[keys.length];
    for ( int i = 0 ; i < keys.length ; i++ )
    {
      result[i] = ApplicationContext.getInstance().getText(paramName + ".value." + keys[i]);
    }
    return result;
  }

  /**
   * Creates new instance.
   *
   * @param name
   * @param matchValue
   * @param allowed
   * @param flags
   */
  protected FixedEnumeration(String name, int matchValue, String[] allowed, Flag... flags)
  {
    super(name, matchValue, flags);
    allowedValues = Collections.unmodifiableList(Arrays.asList(allowed));
  }

  @Override
  public String check(String value, DescribedObject context)
  {
    return getAllowedValues(context).contains(value) ? null : "msg.error.invalidOption";
  }

  private final List<String> allowedValues;

  @Override
  public List<String> getAllowedValues(DescribedObject context)
  {
    return allowedValues;
  }

  @Override
  public Question getQuestion(DescribedObject object)
  {
    TextChoiceQuestion result = createQuestion(object,
                                               (text, group) -> new TextChoiceQuestion(getName(), text,
                                                                                       group));
    List<String> options = new ArrayList<>();
    getAllowedValues(object).forEach(v -> options.add(toDisplayValue(v)));
    options.add(NULL_PLACEHOLDER);
    result.setOptions(options);
    return result;
  }


}
