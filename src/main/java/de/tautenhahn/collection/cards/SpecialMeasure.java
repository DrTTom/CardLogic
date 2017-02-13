package de.tautenhahn.collection.cards;

import de.tautenhahn.collection.generic.data.AttributeInterpreter;
import de.tautenhahn.collection.generic.data.Question;


public class SpecialMeasure extends AttributeInterpreter
{

  protected SpecialMeasure()
  {
    super("specialMeasure");
  }

  @Override
  public boolean isLegalValue(String value)
  {
    return value.matches("([1-9][0-9]*x[1-9][0-9]*)|0");
  }

  @Override
  protected int correllateValue(String thisValue, String otherValue)
  {
    if (thisValue.equals(otherValue))
    {
      return 50;
    }
    if ("0".equals(thisValue) || "0".equals(otherValue))
    {
      return 0; // cannot decide uniquely if eye is visible and has unique position
    }
    if (thisValue.equals(otherValue))
    {
      return 50;
    }
    try
    {
      return new Format.Rectangle(thisValue).similar(new Format.Rectangle(otherValue)) ? 30 : -1;
    }
    catch (IllegalArgumentException e)
    {
      return 0;
    }
  }

  @Override
  public Question getQuestion()
  {
    Question result = new Question("specialMeasure",
                                   "Wo gemessen von links oben befindet sich das rechte Auge des höchsten Königs?",
                                   "Messen und zählen");
    result.setHelptext("'0' if the King of Clubs, Kreuz, Eichel or Swords does not exist or has no recognizable face "
                       + "AxB where A is the distance in mm form left border (of card or circumscribing rectangle) to right eye "
                       + "of the king and B is the distance form upper border to that eye. If only the left eye is shown, that is "
                       + "used instead.");
    return result;
  }


}
