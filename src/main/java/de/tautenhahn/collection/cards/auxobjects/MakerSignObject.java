package de.tautenhahn.collection.cards.auxobjects;

import de.tautenhahn.collection.cards.deck.Maker;
import de.tautenhahn.collection.generic.data.AttributeInterpreter.Flag;
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
    super("makerSign", true, new FreeText("name", 80, 1), new Year("usedFrom"),
          new Year("usedTo", Flag.OPTIONAL), new ImageRef(), new Maker(),
          new FreeText("remark", 80, 4, Flag.OPTIONAL, Flag.SEARCHABLE));
    ((Year)getAttributeInterpreter("usedTo")).addNotBeforeRestriction("usedFrom");
  }

}
