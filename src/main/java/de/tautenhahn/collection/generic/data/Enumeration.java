package de.tautenhahn.collection.generic.data;

import java.util.List;


public abstract class Enumeration extends InterpretedAttribute
{

  protected Enumeration(String name, Flag[] flags)
  {
    super(name, flags);
  }

  private int matchValue;

  public abstract List<String> getAllowedValues();


  @Override
  boolean isLegalValue()
  {
    return (isOptional() && value == null) || getAllowedValues().contains(value);
  }

  @Override
  protected int correllateValue(String otherValue)
  {
    return value.equals(otherValue) ? matchValue : (isExact() ? -1 : 0);
  }
}
