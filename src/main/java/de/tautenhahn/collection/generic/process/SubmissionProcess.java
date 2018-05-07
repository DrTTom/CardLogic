package de.tautenhahn.collection.generic.process;

import java.util.Map;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.DescribedObjectInterpreter;
import de.tautenhahn.collection.generic.persistence.Persistence;


/**
 * Creates a new object in storage.
 *
 * @author TT
 */
public class SubmissionProcess
{

  private final DescribedObjectInterpreter interpreter;

  String type;

  /**
   * Creates an instance for one time use.
   *
   * @param type
   */
  public SubmissionProcess(String type)
  {
    interpreter = ApplicationContext.getInstance().getInterpreter(type);
    this.type = type;
  }

  /**
   * Creates and stores new object
   *
   * @param candidate
   * @param force
   */
  public SubmissionResult submit(DescribedObject candidate, boolean force)
  {
    if (candidate.getPrimKey() != null || !type.equals(candidate.getType()))
    {
      return new SubmissionResult("msg.error.illegalCall", null, false);
    }
    if (!force && interpreter.getQuestions(candidate, true).stream().anyMatch(q -> q.getProblem() != null))
    {
      return new SubmissionResult("msg.error.remainingProblems", null, false);
    }
    String newPrimKey = interpreter.proposeNewPrimKey(candidate);
    ApplicationContext.getInstance()
                      .getPersistence()
                      .store(interpreter.createObject(newPrimKey, candidate.getAttributes()));
    return new SubmissionResult("msg.ok.objectStored", newPrimKey, true);
  }

  /**
   * Updates and stores an object, overwriting the old one.
   *
   * @param primKey
   * @param attributes
   * @param force
   */
  public SubmissionResult update(String primKey, Map<String, String> attributes, boolean force)
  {
    Persistence persistence = ApplicationContext.getInstance().getPersistence();
    DescribedObject existing = persistence.find(type, primKey);
    existing.getAttributes().putAll(attributes);
    return submit(existing, force);
  }
}
