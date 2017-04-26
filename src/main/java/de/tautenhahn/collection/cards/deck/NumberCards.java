package de.tautenhahn.collection.cards.deck;

import de.tautenhahn.collection.generic.data.AttributeInterpreter;
import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.Similarity;


/**
 * Number of cards in a deck - same deck may be marketed with different numbers of cards.
 * 
 * @author TT
 */
public class NumberCards extends AttributeInterpreter
{

  /**
   * Creates immutable instance.
   */
  protected NumberCards()
  {
    super("numberCards");
  }

  @Override
  public String check(String value, DescribedObject context)
  {
    return value.matches("[1-9][0-9]*") ? null : "msg.error.invalidValue";
  }

  @Override
  protected Similarity correllateValue(String thisValue, String otherValue, DescribedObject context)
  {
    if ("110".equals(thisValue) && "55".equals(otherValue) || thisValue.equals(otherValue))
    {
      return Similarity.HINT;
    }
    return Similarity.NO_STATEMENT;
  }

}
