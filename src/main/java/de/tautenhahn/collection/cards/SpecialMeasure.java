package de.tautenhahn.collection.cards;

import java.util.StringTokenizer;

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
    return new Rectangle(thisValue).similar(new Rectangle(otherValue)) ? 30 : -1;
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

  static class Rectangle
  {

    int width, height;

    Rectangle(String value)
    {
      StringTokenizer tokens = new StringTokenizer(value, "x (");
      try
      {
        if (tokens.hasMoreTokens())
        {
          width = Integer.parseInt(tokens.nextToken());
        }
        if (tokens.hasMoreTokens())
        {
          height = Integer.parseInt(tokens.nextToken());
        }
      }
      catch (NumberFormatException e)
      {
        // cannot handle
      }
    }

    boolean similar(Rectangle other)
    {
      return Math.abs(width - other.width) < 2 && Math.abs(height - other.height) < 2;
    }
  }
}
