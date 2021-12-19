package de.tautenhahn.collection.generic.data;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;


/**
 * Base class for all collected items and virtual entities describing the collection.
 *
 * @author TT
 */
@Data
@AllArgsConstructor
public class DescribedObject
{

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

  /**
   * Creates instance with empty attribute map.
   *
   * @param type kind of object
   * @param primKey primary key
   */
  public DescribedObject(String type, String primKey)
  {
    this(type, primKey, new HashMap<>());
  }

  /**
   * Same as outer class but states how much the object matches the search criteria.
   */
  @EqualsAndHashCode(callSuper = true)
  public static class Matched extends DescribedObject
  {

    @Getter
    final int matchValue;

    /**
     * Creates immutable instance.
     * 
     * @param data object to wrap
     * @param similarity defines how much it matches the search criteria
     */
    public Matched(DescribedObject data, Similarity similarity)
    {
      super(data.type, data.primKey, data.attributes);
      matchValue = similarity.getValue();
    }
  }
}
