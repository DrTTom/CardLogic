package de.tautenhahn.collection.cards.auxobjects;

import java.util.Collection;
import java.util.Collections;

import de.tautenhahn.collection.generic.data.FreeText;
import de.tautenhahn.collection.generic.data.ImageRef;
import de.tautenhahn.collection.generic.data.MapBasedDescribedObjectInterpreter;
import de.tautenhahn.collection.generic.data.Year;


/**
 * Describes the tax stamp occurring on a deck.
 *
 * @author TT
 */
public class TaxStampObject extends MapBasedDescribedObjectInterpreter
{

  /**
   * Creates immutable instance
   */
  public TaxStampObject()
  {
    super("taxStamp", true, new FreeText("name", 80, 1), new Year("usedFrom"), new Year("usedTo"), new ImageRef(),
          new FreeText("remark", 80, 2));
  }

}
