package de.tautenhahn.collection.generic.data;

import java.util.List;

import de.tautenhahn.collection.generic.data.question.Question;
import lombok.AllArgsConstructor;
import lombok.Data;


/**
 * Response to some call which submits data to the collection.
 * 
 * @author jean
 */
@Data
@AllArgsConstructor
public class SubmissionResponse
{

  /**
   * Message to the user.
   */
  private String message;

  /**
   * Primary key of created object. May be omitted in case of updating an existing one.
   */
  private String primaryKey;

  /**
   * Contains Questions about all contained attributes. Optional, only returned in case of inconsistent object
   * data.
   */
  private List<Question> questions;

}
