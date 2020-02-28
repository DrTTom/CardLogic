package de.tautenhahn.collection.cards.deck;

import java.util.StringTokenizer;

import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.FreeText;
import de.tautenhahn.collection.generic.data.Similarity;


/**
 * Format of a deck. Similarity accounts for possible errors in measurement.
 *
 * @author TT
 */
public class Format extends FreeText
{

  /**
   * Creates new immutable instance.
   */
  protected Format()
  {
    super("format", 40, 1);
  }

  @Override
  public String check(String value, DescribedObject context)
  {
    return value.matches("[1-9][0-9]*x[1-9][0-9]*( \\(.*\\))?") ? null : "msg.error.invalidValue";
  }

  @Override
  protected Similarity correlateValue(String thisValue, String otherValue, DescribedObject context)
  {
    if (thisValue.equals(otherValue))
    {
      return Similarity.SIMILAR;
    }
    try
    {
      return new Rectangle(thisValue).similar(new Rectangle(otherValue)) ? Similarity.ALMOST_SIMILAR
        : Similarity.DIFFERENT;
    }
    catch (@SuppressWarnings("unused") IllegalArgumentException e)
    {
      return Similarity.NO_STATEMENT;
    }
  }

  /**
   * Just wraps width and height.
   */
  static class Rectangle
  {

    int width;

    int height;

    Rectangle(String value)
    {
      // TODO: use regex!
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
