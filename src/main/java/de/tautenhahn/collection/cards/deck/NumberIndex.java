package de.tautenhahn.collection.cards.deck;

import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.FreeText;
import de.tautenhahn.collection.generic.data.Similarity;
import de.tautenhahn.collection.generic.data.question.Question;


/**
 * Number of index characters on each card.
 *
 * @author TT
 */
public class NumberIndex extends FreeText
{

  /**
   * Creates immutable instance.
   */
  protected NumberIndex()
  {
    super("numIndex", 4, 1);
  }

  @Override
  public String check(String value, DescribedObject context)
  {
    return value.matches("[1-9][0-9]*(/[1-9][0-9]*)*") ? null : "msg.error.invalidValue";
  }

    @Override
    protected Similarity correlateValue(String thisValue, String otherValue, DescribedObject context)
    {
        return thisValue.equals(otherValue) ? Similarity.SIMILAR
                                            : (thisValue.endsWith(otherValue) || otherValue.endsWith(thisValue))
                                              ? Similarity.HINT : Similarity.DIFFERENT;
    }

    @Override
    public Question getQuestion(DescribedObject object)
    {
        Question result = super.getQuestion(object);
        result.setAlignWithPrevious(true);
        return result;
    }
}
