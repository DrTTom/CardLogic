package de.tautenhahn.collection.cards;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.FixedEnumeration;
import de.tautenhahn.collection.generic.data.Question;


public class Suits extends FixedEnumeration
{

  private static final String[] VALUES = new String[]{"french", "german", "spanish", "italian", "swiss",
                                                      "mixed", "other"};

  static
  {
    for ( int i = 0 ; i < VALUES.length ; i++ )
    {
      VALUES[i] = ApplicationContext.getInstance().getText("suits.value." + VALUES[i]);
    }
  }

  public Suits()
  {
    super("suits", 10, VALUES, Flag.EXACT);
  }

  @Override
  public Question getQuestion()
  {
    Question result = new Question("suits", "Was für Farben hat das Spiel?", "Messen und zählen");
    result.setHelptext("Die Dinger in den Ecken!");
    result.setAllowedValues(getAllowedValues());
    return result;
  }
}
