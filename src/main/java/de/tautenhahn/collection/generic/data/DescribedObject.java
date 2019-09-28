package de.tautenhahn.collection.generic.data;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * Base class for all collected items and virtual entities describing the collection.
 *
 * @author TT
 */
@Data
@AllArgsConstructor
public class DescribedObject
{


  public DescribedObject(String type, String primKey)
  {
    this(type, primKey, new HashMap<>());
  }

  /**
   * Default key to store the display name of the object in - in most cases the name is handled as normal
   * attribute.
   */
  public static final String NAME_KEY = "name";

  /**
   * Default key to store an image (relative URL) in. Supports image choices.
   */
  public static final String IMAGE_KEY = "image";

  private final String type;

  private final String primKey;

  private final Map<String, String> attributes;
}
