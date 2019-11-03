package de.tautenhahn.collection.generic.process;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.persistence.Persistence;


/**
 * Deletes an object.
 * 
 * @author TT
 */
public class DeletionProcess
{

  /**
   * Deletes specified object of OK to do so.
   * 
   * @param type type of object
   * @param primKey primary key specifying object
   * @return message
   */
  public String delete(String type, String primKey)
  {
    Persistence storage = ApplicationContext.getInstance().getPersistence();
    if (!storage.keyExists(type, primKey))
    {
      return "msg.error.notFound";
    }
    if (storage.isReferenced(type, primKey, storage.getObjectTypes().toArray(new String[0])))
    {
      return "msg.error.referenced"; // TODO: better message, including referencing type?
    }
    storage.delete(type, primKey);
    return "msg.ok.deletion";
  }
}
