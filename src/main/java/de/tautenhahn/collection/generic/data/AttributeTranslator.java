package de.tautenhahn.collection.generic.data;

/**
 * Indicates that an object is able to translate between internal data values and display values. In most
 * cases, it matches primKey and name values.
 *
 * @author TT
 */
public interface AttributeTranslator
{

  /**
   * Returns a human readable String uniquely describing the typed object with given key.
   *
   * @param primKey
   * @return
   */
  String toName(String primKey);

  /**
   * Inverse to {@link #toName(String)}.
   */
  String toKey(String name);
}
