package de.tautenhahn.collection.cards.deck;

import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.Year;


public class Bought extends Year
{

  public Bought()
  {
    super("bought");
  }

  @Override
  public String check(String value, DescribedObject context)
  {
    String result = super.check(value, context);
    if (result == null && isBefore(value, context, "printedEarliest"))
    {
      return "msg.error.mustNotBeEarlier.printedEarliest";
    }
    return null;
  }
}
