package de.tautenhahn.collection.generic.data;

import java.util.List;


/**
 * Generic handling of enums bound by one foreign key restriction.
 * 
 * @author TT
 */
public class TypeBasedEnumWithForeignKey extends TypeBasedEnumeration
{

  private final String foreignKey;

  public TypeBasedEnumWithForeignKey(String name, String foreignKey, int matchValue, Flag[] flags)
  {
    super(name, matchValue, flags);
    this.foreignKey = foreignKey;

  }

  @Override
  public List<String> getAllowedValues(DescribedObject context)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected String checkSpecific(String value, DescribedObject context)
  {
    // TODO Auto-generated method stub
    return null;
  }

}
