package de.tautenhahn.collection.generic.data.question;

/**
 * Question for one of several allowed text phrases.
 *
 * @author TT
 */
public class TextChoiceQuestion extends ChoiceQuestion
{

  /**
   * Creates instance.
   *
   * @param paramName
   * @param text
   * @param form
   */
  public TextChoiceQuestion(String paramName, String text, String form)
  {
    super(Type.TextChoice, paramName, text, form);
  }

}
