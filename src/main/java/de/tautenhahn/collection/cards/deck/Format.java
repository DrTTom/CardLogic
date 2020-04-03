package de.tautenhahn.collection.cards.deck;

import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.FreeText;
import de.tautenhahn.collection.generic.data.Rectangle;
import de.tautenhahn.collection.generic.data.Similarity;


/**
 * Format of a deck. Similarity accounts for possible errors in measurement.
 *
 * @author TT
 */
public class Format extends FreeText
{

  /**
   * Creates new immutable instance.
   */
  protected Format()
  {
    super("format", 40, 1);
  }

  @Override
  public String check(String value, DescribedObject context)
  {
    return Rectangle.FORMAT_EXT.matcher(value).matches() ? null : "msg.error.invalidValue";
  }

  @Override
  protected Similarity correlateValue(String thisValue, String otherValue, DescribedObject context)
  {
    if (thisValue.equals(otherValue))
    {
      return Similarity.SIMILAR;
    }
    return similarityByTolerance(thisValue, otherValue);
  }

  static Similarity similarityByTolerance(String thisValue, String otherValue)
  {
    try
    {
      switch (new Rectangle(thisValue).biggestDiffTo(new Rectangle(otherValue)))
      {
        case 0: // NOPMD same as 1
        case 1:
          return Similarity.ALMOST_SIMILAR;
        case 2:
          return Similarity.HINT;
        default:
          return Similarity.DIFFERENT;
      }
    }
    catch (IllegalArgumentException e)
    {
      return Similarity.NO_STATEMENT;
    }
  }
}
