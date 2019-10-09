package de.tautenhahn.collection.generic.data.question;

/**
 * Question for a text value.
 *
 * @author TT
 */
public class TextQuestion extends Question
{

  private int cols;

  private int lines;

  /**
   * Creates instance.
   *
   * @param paramName
   * @param text
   * @param form
   */
  public TextQuestion(String paramName, String text, String form)
  {
    super(Type.TEXT, paramName, text, form);
  }

  /**
   * Specifies expected size of input.
   *
   * @param numLines
   * @param charsPerLine
   */
  public void setFormat(int numLines, int charsPerLine)
  {
    lines = numLines;
    cols = charsPerLine;
  }

  /**
   * Returns the number of characters which should fit into one line.
   */
  public int getCols()
  {
    return cols;
  }

  /**
   * Returns the number of expected lines.
   */
  public int getLines()
  {
    return lines;
  }
}
