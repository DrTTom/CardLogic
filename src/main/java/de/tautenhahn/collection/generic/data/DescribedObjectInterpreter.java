package de.tautenhahn.collection.generic.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * Information about supported features of certain type of {@link DescribedObject}. Instances must be
 * thread-safe and state-less.
 *
 * @author TT
 */
public abstract class DescribedObjectInterpreter
{

  private final String type;

  /**
   * Creates new instance.
   *
   * @param type
   */
  protected DescribedObjectInterpreter(String type)
  {
    this.type = type;
  }

  /**
   * Returns the list of all supported attribute names.
   */
  public abstract Collection<String> getSupportedAttributes();

  /**
   * Returns a list of all attributes backed up by binary objects. <br>
   * TODO: this information should be fed from attribute interpreters.
   */
  public abstract Collection<String> getBinaryValuedAttributes();

  /**
   * Returns the interpreter for attribute of specified name.
   *
   * @param name
   */
  public abstract AttributeInterpreter getAttributeInterpreter(String name);

  public Similarity countSimilarity(DescribedObject searchMask, DescribedObject candidate)
  {
    Similarity result = Similarity.NO_STATEMENT;
    for ( String name : getSupportedAttributes() )
    {
      AttributeInterpreter interpreter = getAttributeInterpreter(name);
      result = result.add(interpreter.computeCorrellation(searchMask.getAttributes().get(name),
                                                          candidate.getAttributes().get(name),
                                                          searchMask));
    }
    return result;
  }

  /**
   * Returns a list of Question objects asking for all the attribute values. These questions must
   * <ul>
   * <li>be pre-filled if attribute is already set in context
   * <li>contain sensible allowed values considering already known attribute values. In case an attribute
   * value is already set, the value must occur in the allowed values even if false.
   * <li>contain messages in case there is a problem with the pre-set value.
   * </ul>
   *
   * @param context
   */
  public List<Question> getQuestions(DescribedObject context, boolean reportMissingValues)
  {
    return getSupportedAttributes().stream()
                                   .map(this::getAttributeInterpreter)
                                   .map(ai -> fillQuestion(ai, context, reportMissingValues))
                                   .collect(Collectors.toList());
  }

  private Question fillQuestion(AttributeInterpreter ai, DescribedObject context, boolean reportMissingValues)
  {
    Question result = ai.getQuestion(context);
    if ("".equals(result.getValue()))
    {
      if (reportMissingValues && !ai.isOptional())
      {
        result.setProblem("msg.error.missingValue");
      }
    }
    else
    {
      result.setProblem(ai.check(result.getValue(), context));
    }
    return result;
  }

  /**
   * Returns a primary key value which is not used so far and suitable for given object.
   *
   * @param candidate
   */
  public String proposeNewPrimKey(DescribedObject candidate)
  {
    return UUID.randomUUID().toString();
  }

  /**
   * Creates an object with given values.
   *
   * @param primKey
   * @param parameters contains translations for some technical key values
   */
  public DescribedObject createObject(String primKey, Map<String, String> parameters)
  {
    Map<String, String> attribs = new HashMap<>();

    for ( String key : getSupportedAttributes() )
    {
      String value = parameters.get(key);
      if (value == null || value.trim().isEmpty())
      {
        continue;
      }
      AttributeInterpreter ai = getAttributeInterpreter(key);
      attribs.put(key, ai instanceof AttributeTranslator ? ((AttributeTranslator)ai).toKey(value) : value);
    }

    return new DescribedObject(type, primKey, attribs);
  }

}
