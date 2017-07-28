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

  /**
   * Default key to store an image (relative URL) in. Supports image choices.
   */
  public static final String IMAGE_KEY = "image";

  private final Map<String, String> attributes = new Hashtable<>();

  /**
   * Creates instance.
   *
   * @param type
   * @param primKey
   * @param attributes
   */
  public DescribedObject(String type, String primKey, Map<String, String> attributes)
  {
    this.type = type;
    this.primKey = primKey;
    System.out.println("FINDME: " + attributes);
    this.attributes.putAll(attributes);
  }

  /**
   * Creates instance.
   *
   * @param type
   * @param primKey
   */
  public DescribedObject(String type, String primKey)
  {
    this.type = type;
    this.primKey = primKey;
  }

  /**
   * Returns the type of object. The value indicates how to interpret the attributes.
   */
  public String getType()
  {
    return type;
  }

  /**
   * Returns the key of the object unique among all objects of same type.
   */
  public String getPrimKey()
  {
    return primKey;
  }

  /**
   * Returns the attribute map which may be manipulated.
   */
  public Map<String, String> getAttributes()
  {
    return attributes;
  }
}
