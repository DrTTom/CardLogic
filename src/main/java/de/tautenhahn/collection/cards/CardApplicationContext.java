package de.tautenhahn.collection.cards;

import java.awt.Image;
import java.util.ResourceBundle;

import javax.swing.Icon;

import de.tautenhahn.collection.cards.auxobjects.MakerData;
import de.tautenhahn.collection.cards.auxobjects.MakerSignObject;
import de.tautenhahn.collection.cards.auxobjects.PatternObject;
import de.tautenhahn.collection.cards.deck.Deck;
import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.DescribedObjectInterpreter;
import de.tautenhahn.collection.generic.persistence.Persistence;
import de.tautenhahn.collection.generic.persistence.WorkspacePersistence;


/**
 * Provides all the card-specific objects to make the generic collection application one which specificly
 * supports playing card collections.
 * 
 * @author TT
 */
public class CardApplicationContext extends ApplicationContext
{

  private final Persistence persistence;

  private final ResourceBundle messages = ResourceBundle.getBundle("de.tautenhahn.collection.cards.CardMessages");

  @Override
  public DescribedObjectInterpreter getInterpreter(String type)
  {
    switch (type)
    {
      case "deck":
        return new Deck();
      case "maker":
        return new MakerData();
      case "makerSign":
        return new MakerSignObject();
      case "pattern":
        return new PatternObject();
      default:
        throw new IllegalArgumentException("unsupported type " + type);
    }
  }

  @Override
  protected String getSpecificText(String key)
  {
    return messages.containsKey(key) ? messages.getString(key) : null;
  }

  @Override
  public Icon getApplicationIcon()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Image getNoImage()
  {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * Make the current application a Card Collection.
   */
  public static void register()
  {
    if (ApplicationContext.getInstance() instanceof CardApplicationContext)
    {
      return;
    }
    new CardApplicationContext();
  }

  private CardApplicationContext()
  {
    persistence = new WorkspacePersistence();
  }

  @Override
  public Persistence getPersistence()
  {

    return persistence;
  }


}
