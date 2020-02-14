package de.tautenhahn.collection.generic.data.question;

/**
 * Question asking for upload of some binary content
 *
 * @author TT
 */
public class FileQuestion extends Question
{

  private String accept;

  private String proposedRef;

  /**
   * Creates instance.
   *
   * @param paramName
   * @param text
   * @param form
   */
  public FileQuestion(String paramName, String text, String form)
  {
    super("file-upload", paramName, text, form);
  }

  /**
   * Returns proposed value of the accept attribute.
   */
  public String getAccept()
  {
    return accept;
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
   * Returns the reference to use for uploading some new content. It is only a proposal, the front end may
   * choose another reference and write it into the answer.
   */
  public String getProposedRef()
  {
    return proposedRef;
  }

  /**
   * @see #getProposedRef()
   */
  public void setProposedRef(String proposedRef)
  {
    this.proposedRef = proposedRef;
  }
}
