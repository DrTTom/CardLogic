package de.tautenhahn.collection.cards;

import de.tautenhahn.collection.generic.data.FixedEnumeration;


public class Condition extends FixedEnumeration
{

  private static final String[] VALUES = resolveKeys("condition",
                                                     "sealed",
                                                     "mint",
                                                     "used",
                                                     "heavilyUsed",
                                                     "foxed");

  public Condition()
  {
    super("condition", 1, VALUES);
  }
}
