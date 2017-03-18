package de.tautenhahn.collection.generic.data;

import java.util.List;
import java.util.Map;


/**
 * Generic handling of enums bound by one foreign key restriction.
 * 
 * @author TT
 */
public class TypeBasedEnumWithForeignKey extends TypeBasedEnumeration
{

  private final String foreignKey;

  private Map<String, String> foreignKeyByPrimKey;

  public TypeBasedEnumWithForeignKey(String name, String foreignKey, int matchValue, Flag[] flags)
  {
    super(name, matchValue, flags);
    this.foreignKey = foreignKey;

  }

  @Override
  public List<String> getAllowedValues(DescribedObject context)
  {
    List<String> result = super.getAllowedValues(context);
    String foreignKeyValue = realValue(context.getAttributes().get(foreignKey));
    if (foreignKeyValue != null)
    {
      result.removeIf(v -> !foreignKeyValue.equals(foreignKeyByPrimKey.get(v)));
    }
    return result;
  }

  @Override
  protected void registerObject(DescribedObject obj)
  {
    super.registerObject(obj);
    foreignKeyByPrimKey.put(obj.getPrimKey(), obj.getAttributes().get(foreignKey));
  }

  @Override
  protected String checkSpecific(String value, DescribedObject context)
  {
    String primKey = primKeyByName.get(value);
    if (primKey == null)
    {
      return "msg.error.invalidOption";
    }
    String foreignKeyValue = realValue(context.getAttributes().get(foreignKey));
    if (foreignKeyValue == null || foreignKeyValue.equals(foreignKeyByPrimKey.get(primKey)))
    {
      return null;
    }
    return "msg.error.optionMismatches" + foreignKey;
  }
}
