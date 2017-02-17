package de.tautenhahn.collection.cards;

import de.tautenhahn.collection.generic.data.AttributeInterpreter;
import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.Similarity;


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
  protected Similarity correllateValue(String thisValue, String otherValue, DescribedObject context)
  {
    return thisValue.equals(otherValue) ? Similarity.SIMILAR
      : (thisValue.endsWith(otherValue) || otherValue.endsWith(thisValue)) ? Similarity.HINT
        : Similarity.DIFFERENT;
  }

}
