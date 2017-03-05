package de.tautenhahn.collection.process;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.AttributeInterpreter;
import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.DescribedObjectInterpreter;


/**
 * Creates a new object in storage.
 * 
 * @author TT
 */
public class SubmitProcess
{

  private final DescribedObjectInterpreter interpreter;

  String type;

  public SubmitProcess(String type)
  {
    interpreter = ApplicationContext.getInstance().getInterpreter(type);
    this.type = type;
  }

  /**
   * Creates and stores new object
   * 
   * @param attributes
   * @param force
   * @return
   */
  public SubmissionData submit(Map<String, String> attributes, boolean force)
  {
    Map<String, String> problems = new HashMap<>();
    DescribedObject candidate = new DescribedObject(type, null, attributes);
    if (!force)
    {
      for ( String attributeName : interpreter.getSupportedAttributes() )
      {
        String value = attributes.get(attributeName);
        if (value.trim().isEmpty())
        {
          value = null;
        }
        AttributeInterpreter attr = interpreter.getAttributeInterpreter(attributeName);
        Optional.ofNullable(attr.checkValue(value, candidate)).ifPresent(v -> problems.put(attributeName, v));
      }
    }
    if (problems.isEmpty())
    {
      List<String> existingKeys = ApplicationContext.getInstance().getPersistence().getKeyValues(type);
      int i = 1;
      while (existingKeys.contains(Integer.toString(i)))
      {
        i++;
      }
      candidate = candidate.copyTo(Integer.toString(i));
      ApplicationContext.getInstance().getPersistence().store(candidate);
      return new SubmissionData(Collections.emptyList(), "msg.ok.objectStored", Integer.toString(i), true);
    }
    SubmissionData result = new SubmissionData(interpreter.getQuestions(candidate),
                                               "msg.error.remainingProblems", null, false);
    result.getQuestions().forEach(q -> q.setProblem(problems.get(q.getParamName())));
    return result;
  }

  public SubmissionData update(String primKey, Map<String, String> attributes, boolean force)
  {
    // TODO: almost same handling but do not copy
    return null;
  }
}
