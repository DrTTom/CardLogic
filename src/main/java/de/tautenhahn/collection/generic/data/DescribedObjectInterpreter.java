package de.tautenhahn.collection.generic.data;

import java.util.Collection;


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

  public int countSimilarity(DescribedObject searchMask, DescribedObject candidate)
  {
    int result = 0;
    for ( String name : getSupportedAttributes() )
    {
      int contrib = getAttributeInterpreter(name).computeCorrellation(searchMask.getAttributes().get(name),
                                                                      candidate.getAttributes().get(name),
                                                                      searchMask);
      if (contrib < 0)
      {
        return contrib;
      }
      result += contrib;
    }
    return result;
  }

  public abstract Collection<Question> getQuestions(DescribedObject context);
}
