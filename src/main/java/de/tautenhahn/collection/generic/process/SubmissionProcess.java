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

  final String type;

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
   * @param values client site values to be translated and set in the new object.
   * @param force true to store even in case of incomplete/inconsistent values.
   */
  public SubmissionResponse submit(Map<String, String> values, boolean force)
  {
    DescribedObject candidate = interpreter.createObject(null, values);
    return checkAndStore(candidate, !force);
  }

  private SubmissionResponse checkAndStore(DescribedObject data, boolean performChecks)
  {
    List<Question> remainingQuestions = null;
    ApplicationContext app = ApplicationContext.getInstance();
    if (performChecks)
    {
      remainingQuestions = interpreter.getQuestions(data, true);
      if (remainingQuestions.stream().map(Question::getProblem).anyMatch(Objects::nonNull))
      {
        return new SubmissionResponse(app.getText("msg.error.remainingProblems"), false, data.getPrimKey(),
                                      remainingQuestions);
      }
    }
    ApplicationContext.getInstance().getPersistence().store(data);
    return new SubmissionResponse(app.getText("msg.ok.objectStored"), true, data.getPrimKey(), null);
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
    DescribedObject updatedValues = interpreter.createObject("updatedValues", attributes);
    existing.getAttributes().clear();
    updatedValues.getAttributes()
                 .entrySet()
                 .stream()
                 .filter(e -> !e.getValue().isBlank())
                 .forEach(e -> existing.getAttributes().put(e.getKey(), e.getValue()));
    ;
    return checkAndStore(existing, !force);
  }
}
