package de.tautenhahn.collection.cards.deck;

import de.tautenhahn.collection.generic.data.AttributeInterpreter;
import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.Similarity;


/**
 * A special 2-dimensional measurement helping to distinguish decks.
 *
 * @author TT
 */
public class SpecialMeasure extends AttributeInterpreter
{

  /**
   * Creates immutable instance
   */
  protected SpecialMeasure()
  {
    super("specialMeasure");
  }

  @Override
  public String check(String value, DescribedObject context)
  {
    return value.matches("([1-9][0-9]*x[1-9][0-9]*)|0") ? null : "msg.error.invalidValue";
  }

  @Override
  protected Similarity correllateValue(String thisValue, String otherValue, DescribedObject context)
  {
    if (thisValue.equals(otherValue))
    {
      return Similarity.SIMILAR;
    }
    if ("0".equals(thisValue) || "0".equals(otherValue))
    {
      return Similarity.NO_STATEMENT; // cannot decide uniquely if eye is visible and has unique position
    }
    try
    {
      return new Format.Rectangle(thisValue).similar(new Format.Rectangle(otherValue))
        ? Similarity.ALMOST_SIMILAR : Similarity.DIFFERENT;
    }
    catch (IllegalArgumentException e)
    {
      return Similarity.NO_STATEMENT;
    }
  }

}
