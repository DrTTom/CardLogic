package de.tautenhahn.collection.generic.data;

import java.util.Collection;
import java.util.List;


/**
 * Information about supported features of certain type of {@link DescribedObject}.
 *
 * @author TT
 */
public abstract class DescribedObjectInterpreter
{


  /**
   * Returns the list of all supported attribute names.
   */
  public abstract Collection<String> getSupportedAttributes();

  /**
   * Returns a list of all attributes backed up by binary objects.
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

  public abstract List<Question> getQuestions(DescribedObject context);
}
