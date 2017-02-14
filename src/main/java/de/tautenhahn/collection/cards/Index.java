package de.tautenhahn.collection.cards;

import de.tautenhahn.collection.generic.data.AttributeInterpreter;
import de.tautenhahn.collection.generic.data.DescribedObject;


public class Index extends AttributeInterpreter
{

  protected Index()
  {
    super("index", Flag.EXACT);
  }

  @Override
  public boolean isLegalValue(String value, DescribedObject context)
  {
    return true;
  }

  @Override
  protected int correllateValue(String a, String b, DescribedObject context)
  {
    if (a.equals(b))
    {
      return 50;
    }
    if (Character.isDigit(a.charAt(0)) && Character.isDigit(b.charAt(0)) && !a.contains("/")
        && !b.contains("/"))
    {
      // ignore number in this cases - deck may have different number of
      // cards
      int i = 0;
      while (i < a.length() && !Character.isLetter(a.charAt(i)))
      {
        i++;
      }
      if (i < a.length() && b.endsWith(a.substring(i)))
      {
        return 30;
      }
    }
    return -1;
  }
}
