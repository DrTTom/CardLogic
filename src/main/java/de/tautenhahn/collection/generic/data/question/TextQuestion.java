package de.tautenhahn.collection.generic.data.question;

import lombok.Getter;


/**
 * Question for a text value.
 *
 * @author TT
 */
public class TextQuestion extends Question
{

  @Getter
  private int cols;

  @Getter
  private int rows;

  /**
   * Creates instance.
   *
   * @param paramName
   * @param text
   * @param form
   */
  public TextQuestion(String paramName, String text, String form, int cols, int lines)
  {
    super(lines == 1 ? "text-input" : "bigtext-input", paramName, text, form);
    this.cols = cols;
    this.rows = lines;
  }
}
