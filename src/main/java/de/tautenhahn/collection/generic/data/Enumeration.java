package de.tautenhahn.collection.generic.data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import de.tautenhahn.collection.generic.ApplicationContext;


/**
 * Describes an attribute which has a well-defined number of possible values.
 *
 * @author TT
 */
public abstract class Enumeration extends AttributeInterpreter
{

  /**
   * Place holder to indicate that no value has been set.
   */
  protected static final String NULL_PLACEHOLDER = ApplicationContext.getInstance()
                                                                     .getText("choice.value.null");

  private final int matchValue;

  /**
   * Creates new instance.
   *
   * @param name attribute name
   * @param matchValue describes how strong the hint is that equal values mean equal objects.
   * @param flags define further properties
   */
  protected Enumeration(String name, int matchValue, Flag... flags)
  {
    super(name, flags);
    this.matchValue = matchValue;
  }

  /**
   * Returns the list of legal internal attribute values.
   *
   * @param context other attributes may influence which values are allowed
   * @return allowed values
   */
  public abstract List<String> getAllowedValues(DescribedObject context);

  @Override
  protected Similarity correlateValue(String thisValue, String otherValue, DescribedObject context)
  {
    if (thisValue.equals(otherValue))
    {
      return new Similarity(matchValue);
    }
    return isExact() ? Similarity.DIFFERENT : Similarity.NO_STATEMENT;
  }

  @Override
  public String toDisplayValue(String internalValue)
  {
    return internalValue == null ? NULL_PLACEHOLDER : internalValue;
  }

  /**
   * Prepares the options to chose from
   * 
   * @param object context which may affect allowed values
   * @return key is data value, values is display value, contains null option and current value (even if
   *         illegal)
   */
  protected Map<String, String> getOptions(DescribedObject object)
  {
    Map<String, String> options = new LinkedHashMap<>();
    getAllowedValues(object).forEach(v -> options.put(v, toDisplayValue(v)));
    options.put("", NULL_PLACEHOLDER);
    String currentValue = Optional.ofNullable(object.getAttributes().get(getName())).orElse("");
    if (!options.containsKey(currentValue))
    {
      options.put(currentValue, toDisplayValue(currentValue));
    }
    return options;
  }

}
