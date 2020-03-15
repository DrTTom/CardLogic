package de.tautenhahn.collection.cards.deck;

import java.util.regex.Matcher;

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

  private static final java.util.regex.Pattern IGNORE_NUMBERS = java.util.regex.Pattern.compile("\\d+-?(\\d\\d.*)");

  /**
   * Creates immutable instance.
   */
  protected Index()
  {
    super("index", 20, 1);
  }

  @Override
  protected Similarity correlateValue(String a, String b, DescribedObject context)
  {
    if (a.equals(b))
    {
      return Similarity.SIMILAR;
    }
    Matcher ma = IGNORE_NUMBERS.matcher(a);
    Matcher mb = IGNORE_NUMBERS.matcher(b);
    if (ma.matches() && mb.matches() && ma.group(1).equals(mb.group(1)))
    {
      return Similarity.ALMOST_SIMILAR;
    }
    return Similarity.DIFFERENT;
  }
}
