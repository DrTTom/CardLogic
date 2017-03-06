package de.tautenhahn.collection.generic.data;

import java.util.Hashtable;
import java.util.Map;


/**
 * Base class for all collected items and virtual entities describing the collection.
 *
 * @author TT
 */
public class DescribedObject
{

  private final String type;

  private final String primKey;

  /**
   * Default key to store the display name of the object in - in most cases the name is handled as normal
   * attribute.
   */
  public static final String NAME_KEY = "name";

  private final Map<String, String> attributes = new Hashtable<>();

  public DescribedObject(String type, String primKey, Map<String, String> attributes)
  {
    this.type = type;
    this.primKey = primKey;
    this.attributes.putAll(attributes);
  }

  public DescribedObject(String type, String primKey)
  {
    this.type = type;
    this.primKey = primKey;
  }

  public String getType()
  {
    return type;
  }

  public String getPrimKey()
  {
    return primKey;
  }

  public Map<String, String> getAttributes()
  {
    return attributes;
  }

  public DescribedObject copyTo(String key)
  {
    DescribedObject result = new DescribedObject(type, key);
    result.getAttributes().putAll(attributes);
    return result;
  }
}
