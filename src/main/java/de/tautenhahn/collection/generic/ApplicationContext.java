package de.tautenhahn.collection.generic;

import java.awt.Image;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.swing.Icon;

import de.tautenhahn.collection.generic.data.DescribedObjectInterpreter;
import de.tautenhahn.collection.generic.persistence.Persistence;



/**
 * Produces all the application specific instances of beans. Because the application is very small, a complete
 * Spring context is not needed here. Subclasses are configured application contexts.
 *
 * @author TT
 */
public abstract class ApplicationContext
{

  private static ApplicationContext instance;

  private final ResourceBundle texts = ResourceBundle.getBundle("de.tautenhahn.collection.generic.Messages");

  /**
   * singleton getter
   */
  public static ApplicationContext getInstance()
  {
    return instance;
  }

  /**
   * To be called once at application start.
   */
  protected ApplicationContext()
  {
    if (instance != null)
    {
      throw new IllegalStateException("Application may instanciate only one factory");
    }
    instance = this; // NOPMD only suppressed, TODO: create better API
  }

  /**
   * Return an interpreter for described objects of specified type.
   */
  public abstract DescribedObjectInterpreter getInterpreter(String type);

  /**
   * Return a text specified by key (internationalization).
   *
   * @param key
   */
  public final String getText(String key)
  {
    return texts.containsKey(key) ? texts.getString(key)
      : Optional.ofNullable(getSpecificText(key)).orElse(key);
  }

  /**
   * Returns application specific text for given key.
   */
  protected abstract String getSpecificText(String key);

  /**
   * Returns an icon for the application.
   */
  public abstract Icon getApplicationIcon();

  /**
   * Returns the primary persistence wrapper of the application.
   */
  public abstract Persistence getPersistence();

  /**
   * @return image to be shown in place of missing images.
   */
  public abstract Image getNoImage();

}
