package de.tautenhahn.collection.generic.data;

public class Year extends AttributeInterpreter
{

  public Year(String name, Flag... flags)
  {
    super(name, flags);
  }

  @Override
  public boolean isLegalValue(String value, DescribedObject content)
  {
    return value.matches("[12][0-9][0-9][0-9][0-9]");
  }

  @Override
  protected Similarity correllateValue(String thisValue, String otherValue, DescribedObject content)
  {
    return thisValue.equals(otherValue) ? Similarity.HINT : Similarity.NO_STATEMENT;
  }
}
