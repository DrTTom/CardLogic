package de.tautenhahn.collection.cards;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import de.tautenhahn.collection.generic.data.AttributeInterpreter;
import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.DescribedObjectInterpreter;
import de.tautenhahn.collection.generic.data.ImageRef;
import de.tautenhahn.collection.generic.data.Question;


public class Deck extends DescribedObjectInterpreter
{


  private static final Map<String, AttributeInterpreter> ATTRIBS = new LinkedHashMap<>();
  static
  {
    ATTRIBS.put("suits", new Suits());
    ATTRIBS.put("index", new Index());
    ATTRIBS.put("numIndex", new NumberIndex());
    ATTRIBS.put("format", new Format());
    ATTRIBS.put("specialMeasure", new SpecialMeasure());
    ATTRIBS.put("numberCards", new NumberCards());
    ATTRIBS.put("designer", new Designer());
    ATTRIBS.put("condition", new Condition());
    ATTRIBS.put("location", new Location());
    ATTRIBS.put("image", new ImageRef());
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

  @Override
  public Collection<Question> getQuestions(DescribedObject context)
  {
    return ATTRIBS.values().stream().map(i -> i.getQuestion(context)).collect(Collectors.toList());
  }

}
