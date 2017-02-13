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
  public boolean isLegalValue(String value)
  {
    return isOptional() && value == null || getAllowedValues().contains(value);
  }

  @Override
  protected int correllateValue(String thisValue, String otherValue)
  {
    if (thisValue.equals(otherValue))
    {
      return matchValue;
    }
    return isExact() ? -1 : 0;
  }
}
