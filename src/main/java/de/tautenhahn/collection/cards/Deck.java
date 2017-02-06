package de.tautenhahn.collection.cards;

import de.tautenhahn.collection.generic.data.DescribedObject;


public class Deck extends DescribedObject
{

  private static final String TYPE = "Deck";

  public Deck(DescribedObject data)
  {
    super(checkType(data.getType()), data.getPrimKey());
    getAttributes().putAll(data.getAttributes());
  }

  private static String checkType(String type2)
  {
    if (!TYPE.equals(type2))
    {
      throw new IllegalArgumentException("wrong data type " + type2);
    }
    return type2;
  }


}
