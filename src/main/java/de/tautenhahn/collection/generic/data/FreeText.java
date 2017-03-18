package de.tautenhahn.collection.generic.data;


/**
 * Generic free text property.
 *
 * @author TT
 */
public class FreeText extends AttributeInterpreter
{

  public FreeText(String name, Flag... flags)
  {
    super(name, flags);
  }

  @Override
  public String check(String value, DescribedObject context)
  {
    return null;
  }

  @Override
  protected Similarity correllateValue(String thisValue, String otherValue, DescribedObject context)
  {
    return thisValue.equals(otherValue) ? Similarity.HINT : Similarity.NO_STATEMENT;
  }

}
