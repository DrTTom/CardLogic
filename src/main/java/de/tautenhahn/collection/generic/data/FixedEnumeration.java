package de.tautenhahn.collection.generic.data;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Enumeration with fixed set of allowed values.
 *
 * @author TT
 */
public abstract class FixedEnumeration extends Enumeration
{

  protected FixedEnumeration(String name, int matchValue, String[] allowed, Flag... flags)
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
