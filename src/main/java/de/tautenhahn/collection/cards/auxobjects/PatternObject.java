package de.tautenhahn.collection.cards.auxobjects;

import java.util.Collection;
import java.util.Collections;

import de.tautenhahn.collection.cards.deck.Suits;
import de.tautenhahn.collection.generic.data.FreeText;
import de.tautenhahn.collection.generic.data.ImageRef;
import de.tautenhahn.collection.generic.data.MapBasedDescribedObjectInterpreter;


/**
 * Patterns are stored in persistence, name and primary key are i
 *
 * @author tautenhahn
 */
public class PatternObject extends MapBasedDescribedObjectInterpreter
{


  public PatternObject()
  {
    super("pattern", new FreeText("name"), new ImageRef(), new Suits());
  }

  @Override
  public Collection<String> getBinaryValuedAttributes()
  {
    return Collections.singletonList("image");
  }
}
