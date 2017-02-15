package de.tautenhahn.collection.generic.data;

import java.io.IOException;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.persistence.Persistence;


/**
 * Basic image illustrating the object.
 *
 * @author jean
 */
public class ImageRef extends AttributeInterpreter
{

  public ImageRef(Flag... flags)
  {
    super("image", flags);
  }

  @Override
  public boolean isLegalValue(String value, DescribedObject context)
  {
    return value == null || ApplicationContext.getInstance().getPersistence().binObjectExists(value);
  }

  @Override
  protected int correllateValue(String thisValue, String otherValue, DescribedObject context)
  {
    // TODO: could incorporate image recognition if too much computing power is available
    return 0;
  }

  @Override
  public Question getQuestion(DescribedObject object)
  {
    Question result = super.getQuestion(object);
    Persistence persistence = ApplicationContext.getInstance().getPersistence();
    if (result.getValue() == null || result.getValue().isEmpty())
    {
      if (object.getPrimKey() == null)
      {
        result.setUploadStatus("UNAVAILABLE");
      }
      else
      {
        try
        {
          result.setUploadStatus(persistence.createNewBinRef(object.getPrimKey(), object.getType(), "jpg"));
          // TODO: file extension should reflect file type.
        }
        catch (IOException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
    else
    {
      result.setUploadStatus(persistence.binObjectExists(result.getValue()) ? "COMPLETE" : "MISSING");
    }

    return result;
  }
}
