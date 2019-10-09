package de.tautenhahn.collection.cards.deck;

import de.tautenhahn.collection.generic.data.TypeBasedEnumeration;


/**
 * Represents the company which printed or published a deck.
 *
 * @author TT
 */
public class Maker extends TypeBasedEnumeration
{

  /**
   * Creates immutable instance.
   */
  public Maker()
  {
    super("maker", 50, Flag.OPTIONAL);
  }
}
