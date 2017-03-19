package de.tautenhahn.collection.generic.persistence;

/**
 * Able to remove internally cached data in case of persistence content changed.
 * 
 * @author jean
 */
public interface PersistenceChangeListener
{

  /**
   * Called when persistence content changed.
   * 
   * @param type optional, may indicate that changes are only for one type.
   */
  void onChange(String type);
}
