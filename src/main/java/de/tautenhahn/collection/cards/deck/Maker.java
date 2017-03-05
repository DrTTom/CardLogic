package de.tautenhahn.collection.cards.deck;

import java.util.List;

import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.TypeBasedEnumeration;


public class Maker extends TypeBasedEnumeration
{

  public Maker()
  {
    super("maker", 50, Flag.OPTIONAL);
  }

  @Override
  public List<String> getAllowedValues(DescribedObject context)
  {
    return getPossibleValues();
  }

  @Override
  protected String checkSpecific(String value, DescribedObject context)
  {
    return getAllowedValues(context).contains(value) ? null : "msg.error.invalidOption";
  }

}
