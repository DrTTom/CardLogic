package de.tautenhahn.collection.generic.data.question;

import java.util.List;


/**
 * Question for one of several allowed values.
 *
 * @author TT
 */
public class ChoiceQuestion extends Question
{

  private List<String> options;

  /**
   * Creates instance.
   *
   * @param type
   * @param paramName
   * @param text
   * @param form
   */
  protected ChoiceQuestion(Type type, String paramName, String text, String form)
  {
    super(type, paramName, text, form);
  }

  /**
   * Returns list of options to display in a selection input element.
   *
   * @return null for other types of input
   */
  public List<String> getOptions()
  {
    return options;
  }

  /**
   * Specifies which options to display to the user. Elements are display values, not internal values.
   *
   * @see #getOptions()
   * @param options
   */
  public void setOptions(List<String> options)
  {
    this.options = options;
  }

}
