package de.tautenhahn.collection.generic.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Just wraps width and height for a measure of size or position.
 * 
 * @author tt
 */
public class Rectangle
{

  private static final String WIDTH_X_HEIGHT = "([1-9][0-9]*)x([1-9][0-9]*)";

  /**
   * Pattern used by this class
   */
  public static final Pattern FORMAT = Pattern.compile(WIDTH_X_HEIGHT);

  /**
   * Pattern used by this class + allowing additional remark
   */
  public static final Pattern FORMAT_EXT = Pattern.compile(WIDTH_X_HEIGHT + "( \\(.*\\))?");



  private final int width;

  private final int height;

  /**
   * Creates new instance.
   * 
   * @param value must match format.
   */
  public Rectangle(String value)
  {
    Matcher m = FORMAT_EXT.matcher(value);
    if (m.matches())
    {
      width = Integer.parseInt(m.group(1));
      height = Integer.parseInt(m.group(2));
    }
    else
    {
      throw new IllegalArgumentException();
    }
  }

  /**
   * Return max difference in measures.
   * 
   * @param other to compare to
   * @return maximal difference
   */
  public int biggestDiffTo(Rectangle other)
  {
    return Math.max(Math.abs(width - other.width), Math.abs(height - other.height));
  }
}



