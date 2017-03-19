package de.tautenhahn.collection.cards.deck;

import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.FreeText;
import de.tautenhahn.collection.generic.data.Similarity;


public class Designer extends FreeText
{

  public Designer()
  {
    super("designer", Flag.OPTIONAL);
  }

  @Override
  protected Similarity correllateValue(String thisValue, String otherValue, DescribedObject context)
  {
    return thisValue.equals(otherValue) ? Similarity.HINT : Similarity.NO_STATEMENT;
  }

}
