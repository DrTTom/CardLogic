package de.tautenhahn.collection.generic.data;

import java.util.Collections;
import java.util.List;

import de.tautenhahn.collection.generic.ApplicationContext;


/**
 * Enumeration with allowed values defined by existing objects of another type.
 *
 * @author TT
 */
public abstract class TypeBasedEnumeration extends Enumeration
{

  protected TypeBasedEnumeration(String name, int matchValue, Flag... flags)
  {
    super(name, matchValue, flags);
    allowedValues = Collections.unmodifiableList(ApplicationContext.getInstance()
                                                                   .getPersistence()
                                                                   .getKeyValues(name));
  }

  private final List<String> allowedValues;

  @Override
  public List<String> getAllowedValues()
  {
    return allowedValues;
  }

  @Override
  public Question getQuestion(DescribedObject object)
  {

    Question result = super.getQuestion(object);
    result.setAuxObjectBase("/view/" + getName());
    return result;
  }
}
