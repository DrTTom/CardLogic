package de.tautenhahn.collection.generic.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import de.tautenhahn.collection.generic.data.DescribedObject;


/**
 * Wrapper around the eventual persistence layer - may be database or some file.
 *
 * @author TT
 */
public interface Persistence extends AutoCloseable
{

  /**
   * Adds or updates given object.
   *
   * @param item data to store
   */
  void store(DescribedObject item);

  /**
   * Returns object specified by primary key
   *
   * @param type specifies type of object
   * @param primKey primary key value
   */
  DescribedObject find(String type, String primKey);

  /**
   * Returns all object of given type.
   *
   * @param type specifies type of object
   */
  Stream<DescribedObject> findAll(String type);

  /**
   * Returns objects of given type which match specified attribute values.
   *
   * @param type
   * @param exactValues only those values which require exact match
   */
  Stream<DescribedObject> findByRestriction(String type, Map<String, String> exactValues);

  /**
   * Get connection and/or data.
   *
   * @param args depends on implementation
   * @throws IOException in case of any problems
   */
  void init(String... args) throws IOException;

  /**
   * @return the number of items of specified type
   */
  int getNumberItems(String type);

  /**
   * Returns all values of primKey (unique name) of objects of specified type.
   *
   * @param type specifies type of object
   */
  List<String> getKeyValues(String type);

  /**
   * Removes an object. Throw Exception if object is referenced.
   *
   * @param type specifies type of object
   * @param name primary key value of object to remove
   */
  void delete(String type, String name);

  /**
   * Returns true if object is referenced by other object.
   *
   * @param type specifies type of object
   * @param name primary key value of object to remove
   * @param referencingType specifies which kinds of object to search for references
   */
  boolean isReferenced(String type, String name, String... referencingType);

  /**
   * Returns true if specified object exists.
   *
   * @param type
   * @param name
   */
  boolean keyExists(String type, String name);

  /**
   * Returns all object types.
   */
  List<String> getObjectTypes();

  /**
   * Stores a binary object.
   *
   * @param ins
   * @param ref
   * @throws IOException
   */
  void store(InputStream ins, String ref) throws IOException;

  String createNewBinRef(String parentsPrimKey, String parentsType, String fileExtension) throws IOException;

  /**
   * Returns a binary object
   *
   * @param ref
   * @throws IOException
   */
  InputStream find(String ref) throws IOException;

  /**
   * Returns true if a binary content is stored under the specified reference.
   *
   * @param ref
   */
  boolean binObjectExists(String ref);

  /**
   * Adds a listener.
   *
   * @param listener is notified as soon as persisted content changed
   */
  void addListener(PersistenceChangeListener listener);
}
