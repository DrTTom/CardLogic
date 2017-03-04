package de.tautenhahn.collection.cards.deck;

import java.util.List;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.TypeBasedEnumeration;


public class MakerSign extends TypeBasedEnumeration
{

  public MakerSign()
  {
    super("makerSign", 50, Flag.OPTIONAL, Flag.EXACT);
  }

  @Override
  public List<String> getAllowedValues(DescribedObject context)
  {
    List<String> result = super.getAllowedValues(context);
    System.out.println("will filter " + result);
    String maker = context.getAttributes().get("maker");
    if (maker != null)
    {
      result.removeIf(p -> !(hasMaker(p, maker) || p.equals(context.getAttributes().get("makerSign"))));
    }
    System.out.println("left values " + result);
    return result;
  }

  private boolean hasMaker(String sign, String maker)
  {
    DescribedObject ms = ApplicationContext.getInstance().getPersistence().find("makerSign", sign);
    String mos = ms == null ? null : ms.getAttributes().get("maker");
    return mos == null || mos.equals(maker);
  }

}
