package de.tautenhahn.collection.generic.data;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.question.FileQuestion;
import de.tautenhahn.collection.generic.data.question.Question;


/**
 * Basic image illustrating the object.
 *
 * @author TT
 */
public class ImageRef extends AttributeInterpreter
{

  /**
   * Creates instance for image property.
   *
   * @param flags
   */
  public ImageRef(Flag... flags)
  {
    super("image", flags);
  }

  @Override
  public String check(String value, DescribedObject context)
  {
    return value == null || ApplicationContext.getInstance().getPersistence().binObjectExists(value) ? null
      : "msg.error.missingReferenceImage";
  }

  @Override
  protected Similarity correllateValue(String thisValue, String otherValue, DescribedObject context)
  {
    return Similarity.NO_STATEMENT; // TODO: could incorporate image recognition if too much computing power
                                    // is available and images are strictly defined
  }

  @Override
  public Question getQuestion(DescribedObject object)
  {
    FileQuestion result = createQuestion(object, (text, form) -> new FileQuestion(getName(), text, form));
    result.setAccept(".jpg,.gif,.png,.tiff");
    return result;
  }
}
