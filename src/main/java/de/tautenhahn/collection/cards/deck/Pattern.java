package de.tautenhahn.collection.cards.deck;

import de.tautenhahn.collection.generic.data.TypeBasedEnumWithForeignKey;


/**
 * Standard patterns or "originell".
 *
 * @author TT
 */
public class Pattern extends TypeBasedEnumWithForeignKey
{

  /**
   * creates immutable instance
   */
  public Pattern()
  {
    super("pattern", "suits", 50, Flag.OPTIONAL);
  }

}
