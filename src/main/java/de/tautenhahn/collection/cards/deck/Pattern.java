package de.tautenhahn.collection.cards.deck;

import java.util.List;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.TypeBasedEnumeration;


public class Pattern extends TypeBasedEnumeration
{

  public Pattern()
  {
    super("pattern", 50);
  }

  @Override
  public List<String> getAllowedValues(DescribedObject context)
  {
    List<String> result = super.getAllowedValues(context);
    String suits = context.getAttributes().get("suits");
    if (suits != null)
    {
      result.removeIf(p -> !hasSuits(p, suits));
    }
    return result;
  }

  private boolean hasSuits(String pattern, String suits)
  {
    DescribedObject po = ApplicationContext.getInstance().getPersistence().find("pattern", pattern);
    String ps = po == null ? null : po.getAttributes().get("suits");
    return ps == null || suits.equals(ps);
  }
}
