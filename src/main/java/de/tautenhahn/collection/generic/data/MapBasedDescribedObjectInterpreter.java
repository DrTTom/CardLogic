package de.tautenhahn.collection.generic.data;

import java.util.Collection;
import java.util.LinkedHashMap;
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
   * Creates instance with given set of attribute interpreters.
   *
   * @param type
   * @param useKeyPrefix true to use type as prefix for message keys.
   * @param ai
   */
  protected MapBasedDescribedObjectInterpreter(String type, boolean useKeyPrefix, AttributeInterpreter... ai)
  {
    super(type);
    attribs = new LinkedHashMap<>();
    for ( AttributeInterpreter attr : ai )
    {
      if (useKeyPrefix)
      {
        attr.setKeyPrefix(type);
      }
      attribs.put(attr.getName(), attr);
    }
  }

  /**
   * Creates instance with given set of attribute interpreters.
   *
   * @param type
   * @param ai
   */
  protected MapBasedDescribedObjectInterpreter(String type, AttributeInterpreter... ai)
  {
    this(type, false, ai);
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
