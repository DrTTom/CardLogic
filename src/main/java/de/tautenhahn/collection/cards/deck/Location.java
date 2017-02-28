package de.tautenhahn.collection.cards.deck;

import de.tautenhahn.collection.generic.data.AttributeInterpreter;
import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.Similarity;


public class Location extends AttributeInterpreter
{

  protected Location()
  {
    super("location");
  }

  @Override
  public boolean isLegalValue(String value, DescribedObject context)
  {
    return true;
  }

  @Override
  protected Similarity correllateValue(String thisValue, String otherValue, DescribedObject context)
  {
    return thisValue.equals(otherValue) ? Similarity.HINT : Similarity.NO_STATEMENT;
  }

}
