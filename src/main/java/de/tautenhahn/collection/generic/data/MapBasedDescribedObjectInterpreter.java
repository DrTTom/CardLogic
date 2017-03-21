package de.tautenhahn.collection.generic.data;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;


/**
 * Basic implementation of DescribedObjectInterpreter holding all the attribute interpreters in a map.
 * 
 * @author TT
 */
public abstract class MapBasedDescribedObjectInterpreter extends DescribedObjectInterpreter
{

  private final Map<String, AttributeInterpreter> attribs;

  /**
   * Creates instance with given map of attribute interpreters.
   * 
   * @param type
   * @param attribs
   */
  protected MapBasedDescribedObjectInterpreter(String type, Map<String, AttributeInterpreter> attribs)
  {
    super(type);
    this.attribs = attribs;
  }

  @Override
  public Collection<String> getSupportedAttributes()
  {
    return attribs.keySet();
  }

  @Override
  public AttributeInterpreter getAttributeInterpreter(String name)
  {
    return Optional.ofNullable(attribs.get(name))
                   .orElseThrow(() -> new IllegalArgumentException("unsupported attribute " + name));
  }

}
