package de.tautenhahn.collection.process;

/**
 * Result type of a submission (new object or edited). Since the search will remain always active, this class
 * inherits all the search stuff.
 * 
 * @author TT
 */
public class SubmissionResult extends SearchResult
{

  private final String status;

  private final String primKey;

  private final boolean done;

  public SubmissionResult(String status, String primKey, boolean done)
  {
    super();
    this.status = status;
    this.primKey = primKey;
    this.done = done;
  }

  /**
   * Return true if data has been successfully stored.
   */
  public boolean isDone()
  {
    return done;
  }

  /**
   * Returns some question-independent status message
   */
  public String getStatus()
  {
    return status;
  }

  /**
   * Returns the primary key of the submitted object - may be null in case object was new and not successfully
   * stored.
   */
  public String getPrimKey()
  {
    return primKey;
  }
}
