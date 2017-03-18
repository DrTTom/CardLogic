package de.tautenhahn.collection.cards.deck;

import de.tautenhahn.collection.generic.data.TypeBasedEnumWithForeignKey;


public class Pattern extends TypeBasedEnumWithForeignKey
{

  public Pattern()
  {
    super("pattern", "suits", 50, Flag.OPTIONAL);
  }

}
