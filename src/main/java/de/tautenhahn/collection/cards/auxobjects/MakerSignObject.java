package de.tautenhahn.collection.cards.auxobjects;

import java.util.Collection;
import java.util.Collections;

import de.tautenhahn.collection.cards.deck.Maker;
import de.tautenhahn.collection.generic.data.FreeText;
import de.tautenhahn.collection.generic.data.ImageRef;
import de.tautenhahn.collection.generic.data.MapBasedDescribedObjectInterpreter;
import de.tautenhahn.collection.generic.data.Year;


public class MakerSignObject extends MapBasedDescribedObjectInterpreter
{

  public MakerSignObject()
  {
    super("makerSign", new Year("usedFrom"), new Year("usedTo"), new ImageRef(), new Maker(),
          new FreeText("remark"));
  }

  @Override
  public Collection<String> getBinaryValuedAttributes()
  {
    return Collections.emptyList();
  }

}
