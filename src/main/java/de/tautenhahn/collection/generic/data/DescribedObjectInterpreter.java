package de.tautenhahn.collection.generic.data;

import java.util.Collection;


/**
 * Information about supported features of certain type of {@link DescribedObject}.
 *
 * @author TT
 */
public abstract class DescribedObjectInterpreter
{



  /**
   * Returns a list of AttributeInterpreters for all attributes supported by the given type.
   */
  public abstract Collection<AttributeInterpreter> getSupportedAttributes();

  public AttributeInterpreter getAttributeInterpreter(String name)
  {
    return getSupportedAttributes().stream().filter(a -> name.equals(a.getName())).findAny().orElse(null);
  }
}
