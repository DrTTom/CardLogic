package de.tautenhahn.collection.generic.process;

import java.util.Map;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.DescribedObjectInterpreter;


/**
 * Creates a new object in storage.
 * 
 * @author TT
 */
public class SubmissionProcess
{

  private final DescribedObjectInterpreter interpreter;

  String type;

  public SubmissionProcess(String type)
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
  public SubmissionResult submit(DescribedObject candidate, boolean force)
  {
    if (candidate.getPrimKey() != null || !type.equals(candidate.getType()))
    {
      return new SubmissionResult("msg.error.illegalCall", null, false);
    }
    interpreter.getQuestions(candidate, true)
               .stream()
               .filter(q -> q.getProblem() != null)
               .forEach(q -> System.out.println(q.getParamName() + " " + q.getAllowedValues()));
    if (!force && interpreter.getQuestions(candidate, true).stream().anyMatch(q -> q.getProblem() != null))
    {
      return new SubmissionResult("msg.error.remainingProblems", null, false);
    }
    String newPrimKey = interpreter.proposeNewPrimKey(candidate);
    candidate = interpreter.createObject(newPrimKey, candidate.getAttributes());

    ApplicationContext.getInstance().getPersistence().store(candidate);
    return new SubmissionResult("msg.ok.objectStored", newPrimKey, true);
  }


  public SubmissionResult update(String primKey, Map<String, String> attributes, boolean force)
  {
    // TODO: almost same handling but do not copy
    return null;
  }
}
