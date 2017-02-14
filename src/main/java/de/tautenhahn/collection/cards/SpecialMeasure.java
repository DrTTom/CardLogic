package de.tautenhahn.collection.cards;

import de.tautenhahn.collection.generic.data.AttributeInterpreter;
import de.tautenhahn.collection.generic.data.DescribedObject;


public class SpecialMeasure extends AttributeInterpreter
{

  protected SpecialMeasure()
  {
    super("specialMeasure");
  }

  @Override
  public boolean isLegalValue(String value, DescribedObject context)
  {
    return value.matches("([1-9][0-9]*x[1-9][0-9]*)|0");
  }

  @Override
  protected int correllateValue(String thisValue, String otherValue, DescribedObject context)
  {
    if (thisValue.equals(otherValue))
    {
      return 50;
    }
    if ("0".equals(thisValue) || "0".equals(otherValue))
    {
      return 0; // cannot decide uniquely if eye is visible and has unique position
    }
    if (thisValue.equals(otherValue))
    {
      return 50;
    }
    try
    {
      return new Format.Rectangle(thisValue).similar(new Format.Rectangle(otherValue)) ? 30 : -1;
    }
    catch (IllegalArgumentException e)
    {
      return 0;
    }
  }

}
