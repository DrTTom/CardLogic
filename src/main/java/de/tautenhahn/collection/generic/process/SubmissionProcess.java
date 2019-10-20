package de.tautenhahn.collection.generic.process;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.DescribedObjectInterpreter;
import de.tautenhahn.collection.generic.data.SubmissionResponse;
import de.tautenhahn.collection.generic.data.question.Question;
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
  public SubmissionResponse submit(DescribedObject candidate, boolean force)
  {
    if (!type.equals(candidate.getType()))
    {
      return new SubmissionResponse("msg.error.illegalCall", null, null);
    }

    List<Question> remainingQuestions = null;
    if (!force)
    {
      remainingQuestions = interpreter.getQuestions(candidate, true);
      if (remainingQuestions.stream().map(Question::getProblem).anyMatch(Objects::nonNull))
      {
        return new SubmissionResponse("msg.error.remainingProblems", null, remainingQuestions);
      }
    }
    String newPrimKey = interpreter.proposeNewPrimKey(candidate);
    ApplicationContext.getInstance()
                      .getPersistence()
                      .store(interpreter.createObject(newPrimKey, candidate.getAttributes()));
    return new SubmissionResponse("msg.ok.objectStored", newPrimKey, null);
  }

  /**
   * Updates and stores an object, overwriting the old one.
   *
   * @param primKey
   * @param attributes
   * @param force
   */
  public SubmissionResponse update(String primKey, Map<String, String> attributes, boolean force)
  {
    Persistence persistence = ApplicationContext.getInstance().getPersistence();
    DescribedObject existing = persistence.find(type, primKey);
    existing.getAttributes().putAll(attributes);
    return submit(existing, force);
  }
}
