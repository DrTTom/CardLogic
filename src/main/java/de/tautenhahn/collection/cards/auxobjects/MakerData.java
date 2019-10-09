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

  /**
   * Creates new instance.
   */
  public MakerData()
  {
    super("maker", new FreeText("fullName", 80, 1), new Year("from"), new Year("to"),
          new FreeText("place", 80, 1), new Domain(), new FreeText("remark", 80, 4));
  }

  @Override
  public Collection<String> getBinaryValuedAttributes()
  {
    return Collections.emptyList();
  }
}
