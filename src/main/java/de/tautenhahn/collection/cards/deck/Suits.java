package de.tautenhahn.collection.cards.deck;

import de.tautenhahn.collection.generic.data.FixedEnumeration;


/**
 * Kind of suits of the cards.
 *
 * @author TT
 */
public class Suits extends FixedEnumeration
{

  private static final String[] VALUES = resolveKeys("suits",
                                                     "french",
                                                     "german",
                                                     "spanish",
                                                     "italian",
                                                     "swiss",
                                                     "mixed",
                                                     "other");

  /**
   * creates immutable instance
   */
  public Suits()
  {
    super("suits", 10, VALUES, Flag.EXACT);
  }
}
