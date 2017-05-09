package de.tautenhahn.collection.generic.data.question;

/**
 * Question asking for upload of some binary content
 *
 * @author TT
 */
public class FileQuestion extends Question
{

  private String accept;

  /**
   * Creates instance.
   *
   * @param paramName
   * @param text
   * @param form
   */
  public FileQuestion(String paramName, String text, String form)
  {
    super(Type.File, paramName, text, form);
  }

  /**
   * Specifies filtered file extensions
   *
   * @param accept for instance ".jpg,.gif"
   */
  public void setAccept(String accept)
  {
    this.accept = accept;
  }

  /**
   * Returns proposed value of the accept attribute.
   */
  public String getAccept()
  {
    return accept;
  }
}
