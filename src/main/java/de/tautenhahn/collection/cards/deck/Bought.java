package de.tautenhahn.collection.cards.deck;

import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.Year;


/**
 * Year when a deck was added to the collection.
 * 
 * @author TT
 */
public class Bought extends Year
{

  /**
   * Creates new instance.
   */
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
