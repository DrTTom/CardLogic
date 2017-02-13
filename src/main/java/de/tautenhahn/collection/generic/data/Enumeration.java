package de.tautenhahn.collection.generic.data;

import java.util.List;


public abstract class Enumeration extends AttributeInterpreter
{

  protected Enumeration(String name, int matchValue, Flag[] flags)
  {
    super(name, flags);
  }

  private int matchValue;

  public abstract List<String> getAllowedValues();


  @Override
  public boolean isLegalValue(String value, DescribedObject context)
  {
    return isOptional() && value == null || getAllowedValues().contains(value);
  }

  @Override
  protected int correllateValue(String thisValue, String otherValue, DescribedObject context)
  {
    if (thisValue.equals(otherValue))
    {
      return matchValue;
    }
    return isExact() ? -1 : 0;
  }

  @Override
  public Question getQuestion(DescribedObject object)
  {

    Question result = super.getQuestion(object);
    result.setAllowedValues(getAllowedValues());
    return result;
  }
}
