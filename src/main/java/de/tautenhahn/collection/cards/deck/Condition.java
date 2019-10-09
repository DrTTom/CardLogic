package de.tautenhahn.collection.cards.deck;

import de.tautenhahn.collection.generic.data.FixedEnumeration;


/**
 * Condition a deck is in.
 *
 * @author TT
 */
public class Condition extends FixedEnumeration
{

  private static final String[] VALUES = resolveKeys("condition",
                                                     "sealed",
                                                     "mint",
                                                     "used",
                                                     "heavilyUsed",
                                                     "foxed");

  /**
   * Creates new instance.
   */
  public Condition()
  {
    super("condition", 1, VALUES);
  }
}
