package de.tautenhahn.collection.generic.data.question;

import lombok.EqualsAndHashCode;


/**
 * Question for one of several allowed text phrases.
 *
 * @author TT
 */
@EqualsAndHashCode(callSuper = true)
public class TextChoiceQuestion extends ChoiceQuestion
{

  /**
   * Creates instance.
   *
   * @param paramName name of the described objects attribute
   * @param text to display in label
   * @param form in which context the question should be displayed
   */
  public TextChoiceQuestion(String paramName, String text, String form)
  {
    super("text-choice", paramName, text, form);
  }
}
