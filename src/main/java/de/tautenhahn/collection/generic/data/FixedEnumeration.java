package de.tautenhahn.collection.generic.data;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class FixedEnumeration extends Enumeration
{

  protected FixedEnumeration(String name, String[] allowed, Flag... flags)
  {
    super(name, flags);
    allowedValues = Collections.unmodifiableList(Arrays.asList(allowed));
  }

  private final List<String> allowedValues;

  @Override
  public List<String> getAllowedValues()
  {
    return allowedValues;
  }
}
