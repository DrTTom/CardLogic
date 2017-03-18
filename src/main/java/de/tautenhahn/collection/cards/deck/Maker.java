package de.tautenhahn.collection.cards.deck;

import de.tautenhahn.collection.generic.data.TypeBasedEnumeration;


public class Maker extends TypeBasedEnumeration
{

  public Maker()
  {
    super("maker", 50, Flag.OPTIONAL);
  }

}
