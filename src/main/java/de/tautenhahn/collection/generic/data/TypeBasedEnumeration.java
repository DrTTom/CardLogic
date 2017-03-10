package de.tautenhahn.collection.generic.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.persistence.Persistence;


/**
 * Enumeration with allowed values defined by existing objects of another type.
 *
 * @author TT
 */
public abstract class TypeBasedEnumeration extends Enumeration implements AttributeTranslator
{

  protected Persistence persistence;

  protected static Map<String, String> primKeyByName = new HashMap<>();

  protected static Map<String, String> nameByPrimKey = new HashMap<>();

  public TypeBasedEnumeration(String name, int matchValue, Flag... flags)
  {
    super(name, matchValue, flags);
    persistence = ApplicationContext.getInstance().getPersistence();
    refresh();
  }

  protected List<String> getPossibleValues()
  {
    return new ArrayList<>(primKeyByName.keySet());
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
  public void refresh()
  {
    primKeyByName.clear();
    nameByPrimKey.clear();
    for ( String key : persistence.getKeyValues(getName()) )
    {
      DescribedObject obj = persistence.find(getName(), key);
      String name = Optional.ofNullable(obj.getAttributes().get(DescribedObject.NAME_KEY)).orElse(key);
      primKeyByName.put(name, key); // TODO: handle alternative names here as well
      nameByPrimKey.put(key, name);
    }
  }
}
