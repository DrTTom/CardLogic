package de.tautenhahn.collection.cards;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import javax.swing.Icon;

import de.tautenhahn.collection.cards.auxobjects.MakerData;
import de.tautenhahn.collection.cards.auxobjects.MakerSignObject;
import de.tautenhahn.collection.cards.auxobjects.PatternObject;
import de.tautenhahn.collection.cards.auxobjects.TaxStampObject;
import de.tautenhahn.collection.cards.deck.Deck;
import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.DescribedObjectInterpreter;
import de.tautenhahn.collection.generic.persistence.Persistence;
import de.tautenhahn.collection.generic.persistence.WorkspacePersistence;


/**
 * Provides all the card-specific objects to make the generic collection application one which specifically
 * supports playing card collections.
 *
 * @author TT
 */
public final class CardApplicationContext extends ApplicationContext
{

  private final Persistence persistence;

  private final ResourceBundle messages = ResourceBundle.getBundle("de.tautenhahn.collection.cards.CardMessages");

  private final Map<String, DescribedObjectInterpreter> interpreters = new HashMap<>();

  private CardApplicationContext()
  {
    persistence = new WorkspacePersistence();
    interpreters.put("deck", new Deck());
    interpreters.put("maker", new MakerData());
    interpreters.put("makerSign", new MakerSignObject());
    interpreters.put("pattern", new PatternObject());
    interpreters.put("taxStamp", new TaxStampObject());
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

  @Override
  public DescribedObjectInterpreter getInterpreter(String type)
  {
    return Optional.ofNullable(interpreters.get(type))
                   .orElseThrow(() -> new IllegalArgumentException("unsupported type " + type));
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
  public Persistence getPersistence()
  {
    return persistence;
  }

  @Override
  public Image getNoImage()
  {
    // TODO Auto-generated method stub
    return null;
  }
}
