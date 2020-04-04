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
   * @param item data to store
   */
  void store(DescribedObject item);

  /**
   * @return object specified by primary key
   * @param type specifies type of object
   * @param primKey primary key value
   */
  DescribedObject find(String type, String primKey);

  /**
   * @return all object of given type.
   * @param type specifies type of object
   */
  Stream<DescribedObject> findAll(String type);

  /**
   * Returns objects of given type which match specified attribute values.
   *
   * @param type object type
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
   * @return all values of primKey (unique name) of objects of specified type.
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
   * @return true if object is referenced by other object.
   * @param type specifies type of object
   * @param name primary key value of object to remove
   * @param referencingType specifies which kinds of object to search for references
   */
  boolean isReferenced(String type, String name, String... referencingType);

  /**
   * @return true if specified object exists.
   * @param type object type
   * @param key value of {@link DescribedObject#getPrimKey()}
   */
  boolean keyExists(String type, String key);

  /**
   * @return all object types.
   */
  List<String> getObjectTypes();

  /**
   * Stores a binary object.
   *
   * @param ins content
   * @param ref reference to store into
   * @throws IOException
   */
  void store(InputStream ins, String ref) throws IOException;

  /**
   * Returns a new reference for storing a binary file.
   * 
   * @param parentsPrimKey (expected) primary key of the DescribedObject owning that binary content
   * @param parentsType type of the owner Object
   * @param fileExtension
   * @return proposed reference, should enable guessing the owner object
   * @throws IOException
   */
  String createNewBinRef(String parentsPrimKey, String parentsType, String fileExtension) throws IOException;

  /**
   * Returns a binary object
   *
   * @param ref specifies which one
   * @throws IOException in case of I/O problems
   * @return contain the binary content
   */
  InputStream find(String ref) throws IOException;

  /**
   * Returns true if a binary content is stored under the specified reference.
   *
   * @param ref specifies the binary object in question.
   */
  boolean binObjectExists(String ref);

  /**
   * Adds a listener.
   *
   * @param listener is notified as soon as persisted content changed
   */
  void addListener(PersistenceChangeListener listener);

  /**
   * Actually saves any cached data into persisted state. After calling this method, the process can be safely
   * terminated.
   * 
   * @throws IOException in case of I/O problems
   */
  void flush() throws IOException;
}
