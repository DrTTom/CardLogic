package de.tautenhahn.collection.cards.deck;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.AttributeInterpreter.Flag;
import de.tautenhahn.collection.generic.data.FreeText;
import de.tautenhahn.collection.generic.data.ImageRef;
import de.tautenhahn.collection.generic.data.MapBasedDescribedObjectInterpreter;
import de.tautenhahn.collection.generic.persistence.Persistence;


/**
 * Represents a deck of playing cards, the primary object in a playing card collection.
 *
 * @author TT
 */
public class Deck extends MapBasedDescribedObjectInterpreter
{

  /**
   * Creates new instance.
   */
  public Deck()
  {
    super("deck", // identify deck:
          new Suits(), new Index(), new NumberIndex(), new Format(), new SpecialMeasure(), new NumberCards(),
          new TaxStamp(),
          // interpretation:
          new FreeText("name", 80, 1, Flag.SEARCHABLE), new Pattern(), new Maker(), new MakerSign(),
          new FreeText("refMaker", 40, 1, Flag.OPTIONAL), new Designer(),
          new FreeText("remark", 80, 4, Flag.SEARCHABLE, Flag.OPTIONAL),
          // add to collection:
          new FreeText("refCat", 80, 1, Flag.OPTIONAL, Flag.SEARCHABLE), new PrintedEarliest(),
          new PrintedLatest(), new Bought(), new Condition(), new ImageRef(Flag.OPTIONAL), new Location());
  }

  @Override
  public String proposeNewPrimKey(Map<String, String> attributes)
  {
    Persistence persistence = ApplicationContext.getInstance().getPersistence(); // NOPMD: TODO
    int i = 1;
    while (persistence.keyExists("deck", Integer.toString(i)))
    {
      i++;
    }
    return Integer.toString(i);
  }
}
