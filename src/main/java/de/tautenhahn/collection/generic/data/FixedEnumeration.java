package de.tautenhahn.collection.generic.data;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.tautenhahn.collection.generic.ApplicationContext;


/**
 * Enumeration with fixed set of allowed values.
 *
 * @author TT
 */
public abstract class FixedEnumeration extends Enumeration
{

  protected static String[] resolveKeys(String paramName, String... keys)
  {
    String[] result = new String[keys.length];
    for ( int i = 0 ; i < keys.length ; i++ )
    {
      result[i] = ApplicationContext.getInstance().getText(paramName + ".value." + keys[i]);
    }
    return result;
  }

  protected FixedEnumeration(String name, int matchValue, String[] allowed, Flag... flags)
  {
    super(name, matchValue, flags);
    allowedValues = Collections.unmodifiableList(Arrays.asList(allowed));
  }

  private final List<String> allowedValues;

  @Override
  public List<String> getAllowedValues(DescribedObject context)
  {
    return allowedValues;
  }


}
