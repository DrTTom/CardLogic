package de.tautenhahn.collection.cards;

import java.util.StringTokenizer;

import de.tautenhahn.collection.generic.data.AttributeInterpreter;
import de.tautenhahn.collection.generic.data.DescribedObject;


public class Format extends AttributeInterpreter
{

  protected Format()
  {
    super("format");
  }

  @Override
  public boolean isLegalValue(String value, DescribedObject context)
  {
    return value.matches("[1-9][0-9]*x[1-9][0-9]*( \\(.*\\))?");
  }

  @Override
  protected int correllateValue(String thisValue, String otherValue, DescribedObject context)
  {
    if (thisValue.equals(otherValue))
    {
      return 50;
    }
    try
    {
      return new Rectangle(thisValue).similar(new Rectangle(otherValue)) ? 30 : -1;
    }
    catch (IllegalArgumentException e)
    {
      return 0;
    }

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
        throw new IllegalArgumentException(e);
      }
    }

    boolean similar(Rectangle other)
    {
      return Math.abs(width - other.width) < 2 && Math.abs(height - other.height) < 2;
    }
  }
}
