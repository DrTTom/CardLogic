package de.tautenhahn.collection.cards.deck;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.AttributeInterpreter;
import de.tautenhahn.collection.generic.data.AttributeInterpreter.Flag;
import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.FreeText;
import de.tautenhahn.collection.generic.data.ImageRef;
import de.tautenhahn.collection.generic.data.MapBasedDescribedObjectInterpreter;
import de.tautenhahn.collection.generic.data.Year;


/**
 * Represents a deck of playing cards, the primary object in a playing card collection.
 * 
 * @author TT
 */
public class Deck extends MapBasedDescribedObjectInterpreter
{

  private static final Map<String, AttributeInterpreter> ATTRIBS = new LinkedHashMap<>();
  static
  {
    // identify deck:
    ATTRIBS.put("suits", new Suits());
    ATTRIBS.put("index", new Index());
    ATTRIBS.put("numIndex", new NumberIndex());
    ATTRIBS.put("format", new Format());
    ATTRIBS.put("specialMeasure", new SpecialMeasure());
    ATTRIBS.put("numberCards", new NumberCards());
    // interpretation:
    ATTRIBS.put("name", new FreeText("name", Flag.SEARCHABLE));
    ATTRIBS.put("pattern", new Pattern());
    ATTRIBS.put("maker", new Maker());
    ATTRIBS.put("makerSign", new MakerSign());
    ATTRIBS.put("refMaker", new FreeText("refMaker", Flag.OPTIONAL));
    ATTRIBS.put("designer", new Designer());
    ATTRIBS.put("remark", new FreeText("remark", Flag.SEARCHABLE, Flag.OPTIONAL));
    // add to collection:
    ATTRIBS.put("condition", new Condition());
    ATTRIBS.put("image", new ImageRef(Flag.OPTIONAL));
    ATTRIBS.put("refCat", new FreeText("refCat", Flag.OPTIONAL, Flag.SEARCHABLE));
    ATTRIBS.put("printedEarliest", new Year("printedEarliest", Flag.OPTIONAL));
    ATTRIBS.put("printedLatest", new Year("printedLatest", Flag.OPTIONAL));
    ATTRIBS.put("bought", new Bought());
    ATTRIBS.put("location", new Location());
  }

  /**
   * Creates new instance.
   */
  public Deck()
  {
    super("deck", ATTRIBS);
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
