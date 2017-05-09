package de.tautenhahn.collection.cards.deck;

import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.FreeText;
import de.tautenhahn.collection.generic.data.Similarity;


/**
 * Describes the index of a deck.
 *
 * @author TT
 */
public class Index extends FreeText
{

  /**
   * Creates immutable instance.
   */
  protected Index()
  {
    super("index", 40, 1);
  }

  @Override
  protected Similarity correllateValue(String a, String b, DescribedObject context)
  {
    if (a.equals(b))
    {
      return Similarity.SIMILAR;
    }
    if (Character.isDigit(a.charAt(0)) && Character.isDigit(b.charAt(0)) && !a.contains("/")
        && !b.contains("/"))
    {
      // ignore number in this cases - deck may have different number of
      // cards
      int i = 0;
      while (i < a.length() && !Character.isLetter(a.charAt(i)))
      {
        i++;
      }
      if (i < a.length() && b.endsWith(a.substring(i)))
      {
        return Similarity.ALMOST_SIMILAR;
      }
    }
    return Similarity.DIFFERENT;
  }
}
