package de.tautenhahn.collection.generic.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * Generic handling of enums bound by one foreign key restriction.
 *
 * @author TT
 */
public class TypeBasedEnumWithForeignKey extends TypeBasedEnumeration
{

  private final String foreignKey;

  private final Map<String, String> foreignKeyByPrimKey = new HashMap<>();

  /**
   * Constructs new instance.
   *
   * @param name name of the described attribute
   * @param foreignKey name of attribute which restricts the allowed values. More precisely, the context
   *          object and the object represented by this attribute both have an attribute of that name and the
   *          values must match.
   * @param matchValue indicates significance of matched values.
   * @param flags
   */
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
    String foreignKeyValue = dropEmptyString(context.getAttributes().get(foreignKey));
    if (foreignKeyValue != null)
    {
      result.removeIf(v -> Optional.ofNullable(foreignKeyByPrimKey.get(v))
                                   .map(fk -> !fk.equals(foreignKeyValue))
                                   .orElse(Boolean.FALSE));
    }
    return result;
  }

  @Override
  public String check(String value, DescribedObject context)
  {
    return Optional.ofNullable(super.check(value, context)).orElseGet(() -> {
      String foreignKeyValue = dropEmptyString(context.getAttributes().get(foreignKey));
      if (foreignKeyValue == null || Optional.ofNullable(foreignKeyByPrimKey.get(value))
                                             .map(foreignKeyValue::equals)
                                             .orElse(Boolean.TRUE))
      {
        return null;
      }
      return "msg.error.optionMismatches." + foreignKey;
    });
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

  private void setupForeignKey()
  {
    for ( String key : persistence.getKeyValues(getName()) )
    {
      DescribedObject obj = persistence.find(getName(), key);
      foreignKeyByPrimKey.put(key, obj.getAttributes().get(foreignKey));
    }
  }
}
