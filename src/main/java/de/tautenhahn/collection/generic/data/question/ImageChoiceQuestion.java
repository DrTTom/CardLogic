package de.tautenhahn.collection.generic.data.question;

import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.Getter;


/**
 * Question for one of several allowed images. Answer is still a String but the user is to be shown an image
 * for each option.
 *
 * @author TT
 */
@EqualsAndHashCode(callSuper = true)
public class ImageChoiceQuestion extends ObjectChoiceQuestion
{

  private Map<String, String> urls;

  @Getter
  private String width;

  @Getter
  private String height;

  /**
   * Creates instance.
   *
   * @param paramName attribute name
   * @param text label text
   * @param form question group
   * @param auxType type of object represented by that value
   */
  public ImageChoiceQuestion(String paramName, String text, String form, String auxType)
  {
    super("image-choice", paramName, text, form, auxType);
  }

  /**
   * Returns a map which contains for each allowed value an URL to an image to show.
   */
  public Map<String, String> getUrls()
  {
    return urls;
  }

  /**
   * @param urls
   * @see #getUrls()
   */
  public void setUrls(Map<String, String> urls)
  {
    this.urls = urls;
  }

  /**
   * Specifies how to scale the images. Allowed values are numbers or "auto".
   *
   * @param w
   * @param h
   */
  public void setFormat(String w, String h)
  {
    width = w;
    height = h;
  }
}
