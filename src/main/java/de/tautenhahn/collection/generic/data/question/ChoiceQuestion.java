package de.tautenhahn.collection.generic.data.question;

import java.util.List;


/**
 * Question for one of several allowed values.
 *
 * @author TT
 */
public class ChoiceQuestion extends Question
{

  private List<String> allowedValues;

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
   * Optional, returns list of allowed values if a selection is to be shown.
   *
   * @return null for other types of input
   */
  public List<String> getAllowedValues()
  {
    return allowedValues;
  }

  /**
   * @see #getAllowedValues()
   * @param allowedValues
   */
  public void setAllowedValues(List<String> allowedValues)
  {
    this.allowedValues = allowedValues;
  }

}
