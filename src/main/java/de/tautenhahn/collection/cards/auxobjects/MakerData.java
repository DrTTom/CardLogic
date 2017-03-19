package de.tautenhahn.collection.cards.auxobjects;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import de.tautenhahn.collection.generic.data.AttributeInterpreter;
import de.tautenhahn.collection.generic.data.DescribedObjectInterpreter;
import de.tautenhahn.collection.generic.data.FreeText;
import de.tautenhahn.collection.generic.data.Year;


/**
 * @author TT
 */
public class MakerData extends DescribedObjectInterpreter
{

  public MakerData()
  {
    super("maker");
  }

  private static final Map<String, AttributeInterpreter> ATTRIBS = new LinkedHashMap<>();
  static
  {
    ATTRIBS.put("fullName", new FreeText("fullName"));
    ATTRIBS.put("from", new Year("from"));
    ATTRIBS.put("to", new Year("to"));
    ATTRIBS.put("place", new FreeText("place"));
    ATTRIBS.put("domain", new Domain());
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


}
