package de.tautenhahn.collection.cards.deck;

import de.tautenhahn.collection.generic.data.TypeBasedEnumeration;

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
        enableImage("auto", "120");
    }
}
