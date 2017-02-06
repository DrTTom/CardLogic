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

  private final Map<String, String> attributes = new Hashtable<>();

  public DescribedObject(String type, String primKey)
  {
    super();
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
}
