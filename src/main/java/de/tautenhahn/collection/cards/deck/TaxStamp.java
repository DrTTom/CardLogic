package de.tautenhahn.collection.cards.deck;

import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.TypeBasedEnumeration;
import de.tautenhahn.collection.generic.data.question.Question;


/**
 * Defines the tax stamp (stamps only, no tax aces or wrappers) on a deck.
 *
 * @author TT
 */
public class TaxStamp extends TypeBasedEnumeration
{

  /**
   * Creates immutable instance.
   */
  public TaxStamp()
  {
    super("taxStamp", 50, Flag.OPTIONAL);
  }

  @Override
  public Question getQuestion(DescribedObject object)
  {
    return getImageQuestion(object, "auto", "120");
  }
}
