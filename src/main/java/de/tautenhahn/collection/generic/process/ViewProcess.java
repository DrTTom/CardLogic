package de.tautenhahn.collection.generic.process;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.DescribedObject;


/**
 * Provides data for displaying. Data might be passed to edit process. Not sure this class is needed but looks
 * like it fits the overall pattern.
 *
 * @author TT
 */
public class ViewProcess
{

  public DescribedObject getData(String type, String key)
  {
    return ApplicationContext.getInstance().getPersistence().find(type, key);
  }
}
