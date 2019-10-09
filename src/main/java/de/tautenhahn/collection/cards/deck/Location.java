package de.tautenhahn.collection.cards.deck;

import de.tautenhahn.collection.generic.data.FreeText;


/**
 * Free string telling where the collected object is.
 *
 * @author TT
 */
public class Location extends FreeText
{

  /**
   * Creates immutable instance.
   */
  protected Location()
  {
    super("location", 40, 1);
  }
}
