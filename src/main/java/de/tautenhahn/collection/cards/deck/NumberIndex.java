package de.tautenhahn.collection.cards.deck;

import de.tautenhahn.collection.generic.data.AttributeInterpreter;
import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.Similarity;


/**
 * Number of index characters on each card.
 * 
 * @author TT
 */
public class NumberIndex extends AttributeInterpreter
{

  /**
   * Creates immutable instance.
   */
  protected NumberIndex()
  {
    super("numIndex", Flag.EXACT);
  }

  @Override
  public String check(String value, DescribedObject context)
  {
    return value.matches("[1-9][0-9]*") ? null : "msg.error.invalidValue";
  }

  @Override
  protected Similarity correllateValue(String thisValue, String otherValue, DescribedObject context)
  {
    return thisValue.equals(otherValue) ? Similarity.SIMILAR
      : (thisValue.endsWith(otherValue) || otherValue.endsWith(thisValue)) ? Similarity.HINT
        : Similarity.DIFFERENT;
  }

}
