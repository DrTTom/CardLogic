package de.tautenhahn.collection.cards.deck;

import de.tautenhahn.collection.generic.data.TypeBasedEnumWithForeignKey;


/**
 * Defines the makers logo on the playing cards.
 * 
 * @author TT
 */
public class MakerSign extends TypeBasedEnumWithForeignKey
{

  /**
   * Creates immutable instance.
   */
  public MakerSign()
  {
    super("makerSign", "maker", 50, Flag.OPTIONAL, Flag.EXACT);
  }

}
