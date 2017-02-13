package de.tautenhahn.collection.cards;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.FixedEnumeration;
import de.tautenhahn.collection.generic.data.Question;


public class Condition extends FixedEnumeration
{

  private static final String[] VALUES = new String[]{"sealed", "mint", "used", "heavilyUsed", "foxed"};

  static
  {
    for ( int i = 0 ; i < VALUES.length ; i++ )
    {
      VALUES[i] = ApplicationContext.getInstance().getText("condition.value." + VALUES[i]);
    }
  }

  public Condition()
  {
    super("condition", 1, VALUES);
  }

  @Override
  public Question getQuestion()
  {
    Question result = new Question("condition",
                                   ApplicationContext.getInstance().getText("condition.question"),
                                   "Beschreiben");
    result.setHelptext("So angeben wie im MGM-Katalog!");
    result.setAllowedValues(getAllowedValues());
    return result;
  }
}
