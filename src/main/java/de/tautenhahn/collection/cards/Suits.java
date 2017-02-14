package de.tautenhahn.collection.cards;

import de.tautenhahn.collection.generic.data.FixedEnumeration;


public class Suits extends FixedEnumeration
{

  private static final String[] VALUES = resolveKeys("suits",
                                                     "french",
                                                     "german",
                                                     "spanish",
                                                     "italian",
                                                     "swiss",
                                                     "mixed",
                                                     "other");

  public Suits()
  {
    super("suits", 10, VALUES, Flag.EXACT);
  }
}
