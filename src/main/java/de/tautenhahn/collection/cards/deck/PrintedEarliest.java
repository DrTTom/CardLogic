package de.tautenhahn.collection.cards.deck;

import de.tautenhahn.collection.generic.data.Year;


/**
 * Some earliest year the actual deck could have been printed.
 *
 * @author TT
 */
public class PrintedEarliest extends Year
{

  PrintedEarliest()
  {
    super("printedEarliest", Flag.OPTIONAL);
    addNotBeforeRestriction("maker", "from");
    addNotBeforeRestriction("makerSign", "usedFrom");
    addNotBeforeRestriction("taxStamp", "usedFrom");
    addNotAfterRestriction("maker", "to");
    addNotAfterRestriction("makerSign", "usedTo");
    addNotAfterRestriction("taxStamp", "usedTo");
  }
}
