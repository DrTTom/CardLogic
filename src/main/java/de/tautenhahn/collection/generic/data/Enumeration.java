package de.tautenhahn.collection.generic.data;

import java.util.List;

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

  /**
   * Creates new instance.
   *
   * @param name
   * @param matchValue describes how strong the hint is that equal values mean equal objects.
   * @param flags
   */
  protected Enumeration(String name, int matchValue, Flag[] flags)
  {
    super(name, flags);
    this.matchValue = matchValue;
  }

  private final int matchValue;

  /**
   * Returns the list of legal internal attribute values.
   *
   * @param context
   */
  public abstract List<String> getAllowedValues(DescribedObject context);

  @Override
  protected Similarity correllateValue(String thisValue, String otherValue, DescribedObject context)
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

  @Override
  public String toInternalValue(String displayValue)
  {
    return NULL_PLACEHOLDER.equals(displayValue) ? null : displayValue;
  }


}
