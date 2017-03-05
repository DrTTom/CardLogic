package de.tautenhahn.collection.cards.auxobjects;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import de.tautenhahn.collection.cards.deck.Maker;
import de.tautenhahn.collection.generic.data.AttributeInterpreter;
import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.DescribedObjectInterpreter;
import de.tautenhahn.collection.generic.data.FreeText;
import de.tautenhahn.collection.generic.data.ImageRef;
import de.tautenhahn.collection.generic.data.Question;
import de.tautenhahn.collection.generic.data.Year;


public class MakerSignObject extends DescribedObjectInterpreter
{

  private static final Map<String, AttributeInterpreter> ATTRIBS = new LinkedHashMap<>();
  static
  {
    ATTRIBS.put("usedFrom", new Year("usedFrom"));
    ATTRIBS.put("usedTo", new Year("usedTo"));
    ATTRIBS.put("image", new ImageRef());
    ATTRIBS.put("maker", new Maker());
    ATTRIBS.put("remark", new FreeText("remark"));
  }

  @Override
  public Collection<String> getSupportedAttributes()
  {
    return ATTRIBS.keySet();
  }

  @Override
  public Collection<String> getBinaryValuedAttributes()
  {
    return Collections.emptyList();
  }

  @Override
  public AttributeInterpreter getAttributeInterpreter(String name)
  {
    return Optional.ofNullable(ATTRIBS.get(name))
                   .orElseThrow(() -> new IllegalArgumentException("unsupported attribute " + name));

  }

  @Override
  public List<Question> getQuestions(DescribedObject context)
  {
    return ATTRIBS.values().stream().map(i -> i.getQuestion(context)).collect(Collectors.toList());
  }
}
