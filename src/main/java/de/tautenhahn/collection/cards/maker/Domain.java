package de.tautenhahn.collection.cards.maker;

import de.tautenhahn.collection.generic.data.FixedEnumeration;


public class Domain extends FixedEnumeration
{

  protected Domain()
  {
    super("domain", 0, new String[]{"DE", "EU", "WW"}, Flag.EXACT);
  }
}
