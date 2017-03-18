package de.tautenhahn.collection.cards.deck;

import de.tautenhahn.collection.generic.data.TypeBasedEnumeration;


public class MakerSign extends TypeBasedEnumeration
{

  public MakerSign()
  {
    super("makerSign", 50, Flag.OPTIONAL, Flag.EXACT);
  }

}
