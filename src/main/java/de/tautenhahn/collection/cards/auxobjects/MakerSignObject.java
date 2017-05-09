package de.tautenhahn.collection.cards.auxobjects;

import java.util.Collection;
import java.util.Collections;

import de.tautenhahn.collection.cards.deck.Maker;
import de.tautenhahn.collection.generic.data.FreeText;
import de.tautenhahn.collection.generic.data.ImageRef;
import de.tautenhahn.collection.generic.data.MapBasedDescribedObjectInterpreter;
import de.tautenhahn.collection.generic.data.Year;


/**
 * Interprets an object as a description of a makers sign.
 *
 * @author TT
 */
public class MakerSignObject extends MapBasedDescribedObjectInterpreter
{

  /**
   * Creates immutable instance.
   */
  public MakerSignObject()
  {
    super("makerSign", new Year("usedFrom"), new Year("usedTo"), new ImageRef(), new Maker(),
          new FreeText("remark", 80, 4));
  }

  @Override
  public Collection<String> getBinaryValuedAttributes()
  {
    return Collections.emptyList();
  }

}
