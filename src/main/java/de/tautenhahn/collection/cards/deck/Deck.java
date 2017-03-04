package de.tautenhahn.collection.cards.deck;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import de.tautenhahn.collection.generic.data.AttributeInterpreter;
import de.tautenhahn.collection.generic.data.AttributeInterpreter.Flag;
import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.DescribedObjectInterpreter;
import de.tautenhahn.collection.generic.data.FreeText;
import de.tautenhahn.collection.generic.data.ImageRef;
import de.tautenhahn.collection.generic.data.Question;
import de.tautenhahn.collection.generic.data.Year;


public class Deck extends DescribedObjectInterpreter
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
    ATTRIBS.put("remark", new FreeText("remark", Flag.SEARCHABLE));
    // add to collection:
    ATTRIBS.put("condition", new Condition());
    ATTRIBS.put("image", new ImageRef());
    ATTRIBS.put("refCat", new FreeText("refCat", Flag.OPTIONAL, Flag.SEARCHABLE));
    ATTRIBS.put("printedEarliest", new Year("printedEarliest", Flag.OPTIONAL));
    ATTRIBS.put("printedLatest", new Year("printedLatest", Flag.OPTIONAL));
    ATTRIBS.put("bought", new Year("bought"));
    ATTRIBS.put("location", new Location());

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
