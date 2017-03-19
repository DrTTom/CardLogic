package de.tautenhahn.collection.cards.auxobjects;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import de.tautenhahn.collection.cards.deck.Suits;
import de.tautenhahn.collection.generic.data.AttributeInterpreter;
import de.tautenhahn.collection.generic.data.DescribedObjectInterpreter;
import de.tautenhahn.collection.generic.data.ImageRef;


public class PatternObject extends DescribedObjectInterpreter
{


  public PatternObject()
  {
    super("pattern");
  }

  private static final Map<String, AttributeInterpreter> ATTRIBS = new HashMap<>();
  static
  {
    ATTRIBS.put("image", new ImageRef());
    ATTRIBS.put("suits", new Suits());
  }

  @Override
  public Collection<String> getSupportedAttributes()
  {
    return ATTRIBS.keySet();
  }

  @Override
  public Collection<String> getBinaryValuedAttributes()
  {
    return Collections.singletonList("image");
  }

  @Override
  public AttributeInterpreter getAttributeInterpreter(String name)
  {
    return Optional.ofNullable(ATTRIBS.get(name))
                   .orElseThrow(() -> new IllegalArgumentException("unsupported attribute " + name));

  }
}
