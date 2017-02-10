package de.tautenhahn.collection.generic.data;

import java.net.URL;
import java.util.List;


/**
 * Defines how to get information from the frontend / how to ask the user.
 *
 * @author TT
 */
public class Question
{

	private final String paramName;
	private  String value;

  private List<String> allowedValues;

  private final String text;

  private String helptext;

  private URL imageBase;

  private String form;

  private boolean skipped;
  
  private String problem;
  
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

  /**
   * Returns an additional explaining text.
   */
  public String getHelptext()
  {
    return helptext;
  }


  public void setHelptext(String helptext)
  {
    this.helptext = helptext;
  }

  /**
   * Optional, returns an URL to get images to choose from. The values are base/allowedValue.
   *
   * @return null if no images shall be used
   */
  public URL getImageBase()
  {
    return imageBase;
  }


  public void setImageBase(URL imageBase)
  {
    this.imageBase = imageBase;
  }

  /**
   * Returns name of a form this question belongs to. Should match some view template.
   *
   * @return
   */
  public String getForm()
  {
    return form;
  }

  public void setForm(String form)
  {
    this.form = form;
  }

  /**
   * Returns name of parameter to return the answer in.
   *
   * @return
   */
  public String getParamName()
  {
    return paramName;
  }

  /**
   * Returns question text.
   *
   * @return
   */
  public String getText()
  {
    return text;
  }

  /**
   * Creates instance setting the mandatory parameters.
   *
   * @param paramName
   * @param text
   * @param form
   */
  public Question(String paramName, String text, String form)
  {
    super();
    this.paramName = paramName;
    this.text = text;
    this.form = form;
  }




}
