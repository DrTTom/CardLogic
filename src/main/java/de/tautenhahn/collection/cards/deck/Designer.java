package de.tautenhahn.collection.cards.deck;

import de.tautenhahn.collection.generic.data.FreeText;


/**
 * Designer of a deck, so far only a free text. Possibly use an auxiliary object in case there is enough data
 * about playing card designers.
 *
 * @author TT
 */
public class Designer extends FreeText
{

  /**
   * Creates instance.
   */
  public Designer()
  {
    super("designer", 40, 1, Flag.OPTIONAL);
  }
}
