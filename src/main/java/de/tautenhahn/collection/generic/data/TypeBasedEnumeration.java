package de.tautenhahn.collection.generic.data;

import java.util.List;

import de.tautenhahn.collection.generic.ApplicationContext;


/**
 * Enumeration with allowed values defined by existing objects of another type.
 *
 * @author TT
 */
public class TypeBasedEnumeration extends Enumeration
{

  public TypeBasedEnumeration(String name, int matchValue, Flag... flags)
  {
    super(name, matchValue, flags);
  }

  @Override
  public List<String> getAllowedValues(DescribedObject context)
  {
    return ApplicationContext.getInstance().getPersistence().getKeyValues(getName());
  }

  @Override
  public Question getQuestion(DescribedObject object)
  {

    Question result = super.getQuestion(object);
    result.setAuxObjectBase("/view/" + getName());
    return result;
  }
}
