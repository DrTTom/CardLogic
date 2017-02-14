package de.tautenhahn.collection.cards;

import de.tautenhahn.collection.generic.data.AttributeInterpreter;
import de.tautenhahn.collection.generic.data.DescribedObject;


public class NumberIndex extends AttributeInterpreter
{

  protected NumberIndex()
  {
    super("numIndex", Flag.EXACT);
  }

  @Override
  public boolean isLegalValue(String value, DescribedObject context)
  {
    return value.matches("[1-9](/[1-9])*");
  }

  @Override
  protected int correllateValue(String thisValue, String otherValue, DescribedObject context)
  {
    return thisValue.equals(otherValue) ? 30
      : (thisValue.endsWith(otherValue) || otherValue.endsWith(thisValue)) ? 1 : -1;
  }

}
