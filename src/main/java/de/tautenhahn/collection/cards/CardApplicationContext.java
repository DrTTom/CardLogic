package de.tautenhahn.collection.cards;

import java.awt.Image;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.swing.Icon;

import de.tautenhahn.collection.cards.deck.Deck;
import de.tautenhahn.collection.cards.maker.MakerData;
import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.DescribedObjectInterpreter;
import de.tautenhahn.collection.generic.persistence.Persistence;
import de.tautenhahn.collection.generic.persistence.WorkspacePersistence;


public class CardApplicationContext extends ApplicationContext
{

  private final Persistence persistence;

  ResourceBundle messages = ResourceBundle.getBundle("de.tautenhahn.collection.cards.CardMessages");

  @Override
  public DescribedObjectInterpreter getInterpreter(String type)
  {
    switch (type)
    {
      case "deck":
        return new Deck();
      case "maker":
        return new MakerData();
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

  public static void init()
  {
    new CardApplicationContext();

  }

  private CardApplicationContext()
  {
    persistence = new WorkspacePersistence();
    try
    {
      persistence.init("cards");
    }
    catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Override
  public Persistence getPersistence()
  {

    return persistence;
  }


}
