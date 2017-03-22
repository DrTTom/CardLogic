package de.tautenhahn.collection.cards.deck;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.AttributeInterpreter.Flag;
import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.FreeText;
import de.tautenhahn.collection.generic.data.ImageRef;
import de.tautenhahn.collection.generic.data.MapBasedDescribedObjectInterpreter;


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
          // interpretation:
          new FreeText("name", Flag.SEARCHABLE), new Pattern(), new Maker(), new MakerSign(),
          new FreeText("refMaker", Flag.OPTIONAL), new Designer(),
          new FreeText("remark", Flag.SEARCHABLE, Flag.OPTIONAL),
          // add to collection:
          new FreeText("refCat", Flag.OPTIONAL, Flag.SEARCHABLE), new PrintedEarliest(), new PrintedLatest(),
          new Bought(), new Condition(), new ImageRef(Flag.OPTIONAL), new Location());
  }

  @Override
  public Collection<String> getBinaryValuedAttributes()
  {
    return Collections.singletonList("image");
  }

  @Override
  public String proposeNewPrimKey(DescribedObject candidate)
  {
    List<String> existingKeys = ApplicationContext.getInstance().getPersistence().getKeyValues("deck");
    int i = 1;
    while (existingKeys.contains(Integer.toString(i)))
    {
      i++;
    }
    return Integer.toString(i);
  }
}
