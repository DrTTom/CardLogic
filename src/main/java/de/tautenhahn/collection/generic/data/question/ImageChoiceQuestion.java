package de.tautenhahn.collection.generic.data.question;

import java.util.Map;


/**
 * Question for one of several allowed images. Answer is still a String but the user is to be shown an image
 * for each option.
 *
 * @author TT
 */
public class ImageChoiceQuestion extends ChoiceQuestion
{

  private Map<String, String> urls;

  private int width;

  private int height;

  /**
   * Creates instance.
   *
   * @param paramName
   * @param text
   * @param form
   */
  public ImageChoiceQuestion(String paramName, String text, String form)
  {
    super(Type.ImageChoice, paramName, text, form);
  }

  /**
   * Returns a map which contains for each allowed value an URL to an image to show.
   */
  public Map<String, String> getUrls()
  {
    return urls;
  }

  /**
   * @see #getUrls()
   * @param urls
   */
  public void setUrls(Map<String, String> urls)
  {
    this.urls = urls;
  }

  /**
   * Returns width to display images with.
   */
  public int getWidth()
  {
    return width;
  }

  /**
   * Returns height to display images with. May be -1 if ratio is to be preserved.
   */
  public int getHeight()
  {
    return height;
  }

  /**
   * specifies how to scale the images.
   * 
   * @param w
   * @param h
   */
  public void setFormat(int w, int h)
  {
    width = w;
    height = h;
  }
}
