package de.tautenhahn.collection.generic;

import java.awt.Image;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.swing.Icon;

import de.tautenhahn.collection.generic.data.InterpretedAttribute;
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

  private final ResourceBundle texts = null; // TODO

  private Persistence persistence;

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
      throw new RuntimeException("Application may instanciate only one factory");
    }
    instance = this;
  }

  /**
   * return an editor instance for given class
   */
  public abstract InterpretedAttribute getInterpreter(String attributeName, String value);

  /**
   * Return a text specified by key (internationalization).
   *
   * @param key
   */
  public final String getText(String key)
  {
    return Optional.ofNullable(getSpecificText(key)).orElse(texts.getString(key));
  }

  protected abstract String getSpecificText(String key);


  public abstract Icon getApplicationIcon();



  /**
   * return the primary persistence wrapper of the application.
   *
   * @return
   */
  public Persistence getPersistence()
  {
    return persistence;
  }

  /**
   * @return image to be shown in place of missing images.
   */
  public abstract Image getNoImage();

}
