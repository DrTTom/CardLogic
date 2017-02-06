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
public interface Persistence
{

  /**
   * Adds or updates given object.
   *
   * @param item
   */
  void store(DescribedObject item);

  /**
   * Returns object specified by primary key
   *
   * @param primkey
   */
  DescribedObject find(String type, String primKey);

  /**
   * Returns all object of given type.
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
   * Write back all cached data and free resources - object does not have to work after this call.
   *
   * @throws IOException
   */
  void close() throws IOException;

  /**
   * Get connection and/or data.
   *
   * @param args depends on implementation
   * @throws IOException
   */
  void init(String... args) throws IOException;

  /**
   * return the number of items of specified type
   */
  int getNumberItems(String type);

  /**
   * Returns all values of primKey (unique name) of objects of specified type.
   *
   * @param type
   */
  public List<String> getKeyValues(String type);

  /**
   * Removes an object. Throw Exception if object is referenced.
   *
   * @param object
   */
  void delete(String type, String name);

  /**
   * Returns true if object is referenced by other object.
   *
   * @param type
   * @param name
   * @return
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
   * @return
   * @throws IOException
   */
  InputStream find(String ref) throws IOException;

  String search(String type, String query);

  void addToIndex(String type, String key, String data);
}
