package de.tautenhahn.collection.cards;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.FixedEnumeration;


public class Suits extends FixedEnumeration
{

  private static final String[] VALUES = new String[]{"french", "german", "spanish", "italian", "swiss",
                                                      "mixed", "other"};
  static
  {
    for ( int i = 0 ; i < VALUES.length ; i++ )
    {
      VALUES[i] = ApplicationContext.getInstance().getText("value.suits." + VALUES[i]);
    }
  }

  public Suits()
  {
    super("suits", VALUES, Flag.EXACT);
  }
}
