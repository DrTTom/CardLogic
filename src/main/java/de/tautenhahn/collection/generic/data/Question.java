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

  private String value;

  private List<String> allowedValues;

  private final String text;

  private String helptext;

  private URL imageBase;

  private URL auxObjectBase;

  private String form;

  private String problem;

  private String uploadStatus;

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

  /**
   * If an URL is returned, the front end may find explaining images for each allowed value under that URL.
   * 
   * @param imageBase
   */
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

  public String getValue()
  {
    return value;
  }


  public void setValue(String value)
  {
    this.value = value;
  }


  public String getProblem()
  {
    return problem;
  }


  public void setProblem(String problem)
  {
    this.problem = problem;
  }

  /**
   * If an URL is returned, the front end will find a view of a dependent object for each allowed value there.
   */
  public URL getAuxObjectBase()
  {
    return auxObjectBase;
  }


  public void setAuxObjectBase(URL auxObjectBase)
  {
    this.auxObjectBase = auxObjectBase;
  }

  /**
   * Returns a reference String in case a file upload is possible for this value or "COMPLETED" in case that
   * an upload to the value has already been done.
   */
  public String getUploadStatus()
  {
    return uploadStatus;
  }


  public void setUploadStatus(String uploadStatus)
  {
    this.uploadStatus = uploadStatus;
  }



}
