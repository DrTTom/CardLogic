package de.tautenhahn.collection.generic.data.question;

/**
 * Defines how to get information from the front end / how to ask the user.
 *
 * @author TT
 */
public class Question
{

  /**
   * Type of input required to answer the question.
   */
  public enum Type
  {
    /** free text */
    Text,
    /** choice among given text phrases */
    TextChoice,
    /** choice among given images */
    ImageChoice,
    /** choice among given objects by name */
    ObjectChoice,
    /** file upload */
    File
  }

  private final String paramName;

  private String value;

  private final String text;

  private String helptext;

  private final String form;

  private String problem;

  private final Type type;

  /**
   * Creates instance setting the mandatory parameters.
   *
   * @param paramName
   * @param text
   * @param form
   */
  protected Question(Type type, String paramName, String text, String form)
  {
    this.type = type;
    this.paramName = paramName;
    this.text = text;
    this.form = form;
  }



  /**
   * Returns the type of question.
   */
  public Type getType()
  {
    return type;
  }

  /**
   * Returns an additional explaining text.
   */
  public String getHelptext()
  {
    return helptext;
  }

  /**
   * @see #getHelptext()
   * @param helptext
   */
  public void setHelptext(String helptext)
  {
    this.helptext = helptext;
  }

  /**
   * Returns name of a form this question belongs to. Should match some view template.
   */
  public String getForm()
  {
    return form;
  }

  /**
   * Returns name of parameter to return the answer in.
   */
  public String getParamName()
  {
    return paramName;
  }

  /**
   * Returns question text.
   */
  public String getText()
  {
    return text;
  }

  /**
   * Returns the answer if already known
   */
  public String getValue()
  {
    return value;
  }

  /**
   * @see #getValue()
   * @param value
   */
  public void setValue(String value)
  {
    this.value = value;
  }

  /**
   * Returns a human-readable message in case the value is wrong, null if OK.
   */
  public String getProblem()
  {
    return problem;
  }

  /**
   * @see Question#getProblem()
   * @param problem
   */
  public void setProblem(String problem)
  {
    this.problem = problem;
  }

}
