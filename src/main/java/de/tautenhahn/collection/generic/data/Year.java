package de.tautenhahn.collection.generic.data;

public abstract class Year extends AttributeInterpreter
{

  protected Year(String name, Flag[] flags)
  {
    super(name, flags);
  }

  @Override
  public boolean isLegalValue(String value, DescribedObject content)
  {
    return value.matches("[12][0-9][0-9][0-9][0-9]");
  }

  @Override
  protected int correllateValue(String thisValue, String otherValue, DescribedObject content)
  {
    return 0;
  }

  public class Latest extends Year
  {

    @Override
    public boolean isLegalValue(String value, DescribedObject content)
    {
      return value.matches("[12][0-9][0-9][0-9][0-9]");
    }

    protected Latest(String name, Flag[] flags)
    {
      super(name, flags);
    }


  }

  public class Earliest extends Year
  {

    protected Earliest(String name, Flag[] flags)
    {
      super(name, flags);
    }
  }

}
