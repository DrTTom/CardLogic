package de.tautenhahn.collection.generic.data;

import java.util.List;

import de.tautenhahn.collection.generic.data.question.Question;
import lombok.Value;


/**
 * Response to all rest calls which initiate editing some object.
 * 
 * @author TT
 */
@Value
public class DefaultResponse
{

  /**
   * Questions for all undefined
   */
  private List<Question> questions;

  private List<DescribedObject> matchingObjects;
}
