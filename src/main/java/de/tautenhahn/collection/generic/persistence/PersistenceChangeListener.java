package de.tautenhahn.collection.generic.persistence;

/**
 * Ability to react on changes of persistence content (for instance update of remove internally cached data).
 *
 * @author TT
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
