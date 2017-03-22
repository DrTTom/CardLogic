package de.tautenhahn.collection.generic.data;

import java.util.HashMap;
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

  private final Map<String, String> foreignKeyByPrimKey = new HashMap<>();

  public TypeBasedEnumWithForeignKey(String name, String foreignKey, int matchValue, Flag... flags)
  {
    super(name, matchValue, flags);
    this.foreignKey = foreignKey;
    setupForeignKey();
  }

  @Override
  public List<String> getAllowedValues(DescribedObject context)
  {
    List<String> result = super.getAllowedValues(context);
    String foreignKeyValue = realValue(context.getAttributes().get(foreignKey));
    if (foreignKeyValue != null)
    {
      result.removeIf(v -> !foreignKeyValue.equals(foreignKeyByPrimKey.get(toKey(v))));
    }
    return result;
  }

  private void setupForeignKey()
  {
    for ( String key : persistence.getKeyValues(getName()) )
    {
      DescribedObject obj = persistence.find(getName(), key);
      foreignKeyByPrimKey.put(key, obj.getAttributes().get(foreignKey));
    }
  }

  @Override
  public String check(String value, DescribedObject context)
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
    return "msg.error.optionMismatches." + foreignKey;
  }

  @Override
  public void onChange(String type)
  {
    super.onChange(type);
    if (getName().equals(foreignKey) || "*".equals(type))
    {
      setupForeignKey();
    }
  }
}
