package de.tautenhahn.collection.cards.deck;

import de.tautenhahn.collection.generic.data.Year;


/**
 * Year when a deck was added to the collection.
 *
 * @author TT
 */
public class Bought extends Year
{

  /**
   * Creates new instance.
   */
  public Bought()
  {
    super("bought");
    addNotBeforeRestriction("printedLatest");
  }
}
