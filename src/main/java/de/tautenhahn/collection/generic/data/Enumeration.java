package de.tautenhahn.collection.generic.data;

import java.util.List;


public abstract class Enumeration extends AttributeInterpreter
{

  protected Enumeration(String name, int matchValue, Flag[] flags)
  {
    super(name, flags);
    this.matchValue = matchValue;
  }

  private final int matchValue;

  public abstract List<String> getAllowedValues(DescribedObject context);

  @Override
  protected Similarity correllateValue(String thisValue, String otherValue, DescribedObject context)
  {
    if (thisValue.equals(otherValue))
    {
      return new Similarity(matchValue);
    }
    return isExact() ? Similarity.DIFFERENT : Similarity.NO_STATEMENT;
  }

  @Override
  public Question getQuestion(DescribedObject object)
  {
    Question result = super.getQuestion(object);
    result.setAllowedValues(getAllowedValues(object));
    return result;
  }
}
