package de.tautenhahn.collection.generic.data;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Enumeration with allowed values defined by existing objects of another type.
 *
 * @author TT
 */
public abstract class TypeBasedEnumeration extends Enumeration
{

  protected TypeBasedEnumeration(String name, int matchValue, String[] allowed, Flag... flags)
  {
    super(name, matchValue, flags);
    allowedValues = Collections.unmodifiableList(Arrays.asList(allowed));
  }

  private final List<String> allowedValues;

  @Override
  public List<String> getAllowedValues()
  {
    return allowedValues;
  }
}
