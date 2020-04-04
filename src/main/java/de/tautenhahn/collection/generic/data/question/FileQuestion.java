package de.tautenhahn.collection.generic.data.question;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


/**
 * Question asking for upload of some binary content
 *
 * @author TT
 */
@EqualsAndHashCode(callSuper = true)
public class FileQuestion extends Question
{

  @Getter
  @Setter
  private String accept;

  /**
   * Actual or estimated value of the primary key of the surrounding
   * {@link de.tautenhahn.collection.generic.data.DescribedObject}. The client may submit that information
   * when uploading a file so the storage can create a reference with a "speaking" name.
   */
  @Getter
  @Setter
  private String contextKey;

  @Getter
  @Setter
  private String contextType;


  /**
   * Creates instance.
   *
   * @param paramName name of the described objects attribute
   * @param text to display in label
   * @param form in which context the question should be displayed
   */
  public FileQuestion(String paramName, String text, String form)
  {
    super("file-upload", paramName, text, form);
  }
}
