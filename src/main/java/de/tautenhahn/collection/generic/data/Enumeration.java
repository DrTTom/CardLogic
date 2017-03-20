package de.tautenhahn.collection.generic.data;

import java.util.List;


/**
 * Describes an attribute which has a well-defined number of possible values.
 * 
 * @author TT
 */
public abstract class Enumeration extends AttributeInterpreter
{

  protected Enumeration(String name, int matchValue, Flag[] flags)
  {
    super(name, flags);
    this.matchValue = matchValue;
  }

  private final int matchValue;

  /**
   * Returns the list of legal attribute values as used in questions and front end input.
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
  public Question getQuestion(DescribedObject object)
  {
    Question result = super.getQuestion(object);
    result.setAllowedValues(getAllowedValues(object));
    return result;
  }
}
