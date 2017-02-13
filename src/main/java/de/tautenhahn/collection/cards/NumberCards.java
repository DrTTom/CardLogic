package de.tautenhahn.collection.cards;

import de.tautenhahn.collection.generic.data.AttributeInterpreter;
import de.tautenhahn.collection.generic.data.Question;


public class NumberCards extends AttributeInterpreter
{

  protected NumberCards()
  {
    super("numberCards");
  }

  @Override
  public boolean isLegalValue(String value)
  {
    return value.matches("[1-9][0-9]*");
  }

  @Override
  protected int correllateValue(String thisValue, String otherValue)
  {
    if ("110".equals(thisValue) && "55".equals(otherValue))
    {
      return 1;
    }
    return thisValue.equals(otherValue) ? 1 : 0;
  }

  @Override
  public Question getQuestion()
  {
    Question result = new Question("numberCards", "Anzahl der Karten: ", "Messen und zählen");
    result.setHelptext("Das wirst Du ja wohl rauskriegen!!");

    return result;
  }

}
