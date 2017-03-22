package de.tautenhahn.collection.cards.auxobjects;

import java.util.Collection;
import java.util.Collections;

import de.tautenhahn.collection.generic.data.FreeText;
import de.tautenhahn.collection.generic.data.MapBasedDescribedObjectInterpreter;
import de.tautenhahn.collection.generic.data.Year;


/**
 * Describes a playing card factory.
 *
 * @author TT
 */
public class MakerData extends MapBasedDescribedObjectInterpreter
{

  public MakerData()
  {
    super("maker", new FreeText("fullName"), new Year("from"), new Year("to"), new FreeText("place"),
          new Domain(), new FreeText("remark"));
  }

  @Override
  public Collection<String> getBinaryValuedAttributes()
  {
    return Collections.emptyList();
  }

}
