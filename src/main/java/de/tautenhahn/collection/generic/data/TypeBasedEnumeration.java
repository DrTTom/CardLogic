package de.tautenhahn.collection.generic.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.persistence.Persistence;
import de.tautenhahn.collection.generic.persistence.PersistenceChangeListener;


/**
 * Enumeration with allowed values defined by existing objects of another type.
 *
 * @author TT
 */
public abstract class TypeBasedEnumeration extends Enumeration
  implements AttributeTranslator, PersistenceChangeListener
{

  protected Persistence persistence;

  protected Map<String, String> primKeyByName = new TreeMap<>();

  protected Map<String, String> nameByPrimKey = new HashMap<>();

  public TypeBasedEnumeration(String name, int matchValue, Flag... flags)
  {
    super(name, matchValue, flags);
    persistence = ApplicationContext.getInstance().getPersistence();
    persistence.addListener(this);
    setupMaps();
  }

  @Override
  public List<String> getAllowedValues(DescribedObject context)
  {
    ArrayList<String> result = new ArrayList<>();
    result.add("");
    result.addAll(primKeyByName.keySet());
    Optional.ofNullable(context)
            .map(o -> o.getAttributes().get(getName()))
            .map(key -> nameByPrimKey.get(key))
            .filter(name -> !result.contains(name))
            .ifPresent(name -> result.add(name));
    return result;
  }

  @Override
  public Question getQuestion(DescribedObject object)
  {
    Question result = super.getQuestion(object);
    result.setAuxObjectBase("/view/" + getName());
    return result;
  }


  @Override
  public String toName(String primKey)
  {
    return nameByPrimKey.getOrDefault(primKey, primKey);
  }


  @Override
  public String toKey(String name)
  {
    return primKeyByName.getOrDefault(name, name);
  }

  @Override
  public String check(String value, DescribedObject context)
  {
    return primKeyByName.containsKey(value) ? null : "msg.error.invalidOption";
  }

  private void setupMaps()
  {
    for ( String key : persistence.getKeyValues(getName()) )
    {
      DescribedObject obj = persistence.find(getName(), key);
      String name = Optional.ofNullable(obj.getAttributes().get(DescribedObject.NAME_KEY))
                            .orElse(obj.getPrimKey());
      primKeyByName.put(name, obj.getPrimKey()); // TODO: handle alternative names here as well
      nameByPrimKey.put(obj.getPrimKey(), name);
    }
  }

  @Override
  public void onChange(String type)
  {
    if (getName().equals(type) || "*".equals(type))
    {
      setupMaps();
    }
  }

}
