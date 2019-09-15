package de.tautenhahn.collection.generic.data.question;

/**
 * Question for an auxiliary object of some type.
 *
 * @author TT
 */
public class ObjectChoiceQuestion extends ChoiceQuestion
{

  private final String auxType;

  /**
   * Creates instance.
   *
   * @param paramName
   * @param text
   * @param form
   */
  public ObjectChoiceQuestion(String paramName, String text, String form, String auxType)
  {
    super(Type.OBJECT_CHOICE, paramName, text, form);
    this.auxType = auxType;
  }

  /**
   * Returns the type of auxiliary object. TODO: must find out the primary key as well.
   */
  public String getAuxType()
  {
    return auxType;
  }


}
