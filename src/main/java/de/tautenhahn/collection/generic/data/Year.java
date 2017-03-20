package de.tautenhahn.collection.generic.data;

import java.util.Optional;


public class Year extends AttributeInterpreter
{

  public Year(String name, Flag... flags)
  {
    super(name, flags);
  }

  @Override
  public String check(String value, DescribedObject content)
  {
    return value.matches("[12][0-9][0-9][0-9]") ? null : "msg.error.mustBeYear";
  }

  @Override
  protected Similarity correllateValue(String thisValue, String otherValue, DescribedObject content)
  {
    return thisValue.equals(otherValue) ? Similarity.HINT : Similarity.NO_STATEMENT;
  }

  /**
   * Returns <code>true</code> if the current value is before the year-typed attribute specified by
   * parameters. If that attribute is not given, return <code>false</code>.
   * 
   * @param context
   * @param attrib
   */
  protected boolean isBefore(String value, DescribedObject context, String attrib)
  {
    try
    {
      int other = Optional.ofNullable(context)
                          .map(DescribedObject::getAttributes)
                          .map(a -> a.get(attrib))
                          .map(v -> Integer.parseInt(v))
                          .orElse(0);
      return Integer.parseInt(value) < other;
    }
    catch (NumberFormatException e)
    {
      return false;
    }
  }
}
