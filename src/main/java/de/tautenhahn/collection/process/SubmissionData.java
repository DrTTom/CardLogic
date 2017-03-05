package de.tautenhahn.collection.process;

import java.util.List;

import de.tautenhahn.collection.generic.data.Question;


/**
 * Wraps data transported to the front end during submission process.
 * 
 * @author TT
 */
public class SubmissionData
{

  private final List<Question> questions;

  private final String status;

  private final String primKey;

  private final boolean done;

  public SubmissionData(List<Question> questions, String status, String primKey, boolean done)
  {
    this.questions = questions;
    this.status = status;
    this.primKey = primKey;
    this.done = done;
  }



  public boolean isDone()
  {
    return done;
  }


  public List<Question> getQuestions()
  {
    return questions;
  }


  public String getStatus()
  {
    return status;
  }


  public String getPrimKey()
  {
    return primKey;
  }


}
