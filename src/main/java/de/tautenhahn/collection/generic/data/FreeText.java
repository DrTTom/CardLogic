package de.tautenhahn.collection.generic.data;


/**
 * Generic free text property.
 *
 * @author TT
 */
public class FreeText extends AttributeInterpreter
{

  public FreeText(String name)
  {
    super(name, Flag.SEARCHABLE, Flag.OPTIONAL);
  }

  @Override
  public boolean isLegalValue(String value, DescribedObject context)
  {
    return true;
  }

  @Override
  protected Similarity correllateValue(String thisValue, String otherValue, DescribedObject context)
  {
    return thisValue.equals(otherValue) ? Similarity.HINT : Similarity.NO_STATEMENT;
  }

}
