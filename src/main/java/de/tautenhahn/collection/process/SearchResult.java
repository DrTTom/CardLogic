package de.tautenhahn.collection.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.Message;
import de.tautenhahn.collection.generic.data.Question;


/**
 * Contains all data created by a search process. Note that search will also be active during submission or
 * modification of objects.
 *
 * @author TT
 */
public class SearchResult
{

  private String type;

  private List<Question> questions;

  private List<Message> messages;

  private int numberTotal;

  private int numberMatching;

  private int numberPossible;

  private List<DescribedObject> matches = new ArrayList<>();

  private Map<String, Map<String, String>> translations;

  /**
   * Returns type of searched objects.
   */
  public String getType()
  {
    return type;
  }

  /**
   * @see #getType()
   */
  public void setType(String type)
  {
    this.type = type;
  }

  public List<Question> getQuestions()
  {
    return questions;
  }

  public void setQuestions(List<Question> questions)
  {
    this.questions = questions;
  }


  public List<Message> getMessages()
  {
    return messages;
  }

  public void setMessages(List<Message> messages)
  {
    this.messages = messages;
  }

  public int getNumberTotal()
  {
    return numberTotal;
  }

  public void setNumberTotal(int numberTotal)
  {
    this.numberTotal = numberTotal;
  }

  public List<DescribedObject> getMatches()
  {
    return matches;
  }

  public void setMatches(List<DescribedObject> matches)
  {
    this.matches = matches;
  }

  public int getNumberMatching()
  {
    return numberMatching;
  }


  public void setNumberMatching(int numberMatching)
  {
    this.numberMatching = numberMatching;
  }


  public int getNumberPossible()
  {
    return numberPossible;
  }

  public void setNumberPossible(int numberPossible)
  {
    this.numberPossible = numberPossible;
  }

  public Map<String, Map<String, String>> getTranslations()
  {
    return translations;
  }

  public void addTranslation(String attrName, String orig, String translation)
  {
    if (translations == null)
    {
      translations = new HashMap<>();
    }
    Map<String, String> target = translations.get(attrName);
    if (target == null)
    {
      target = new HashMap<>();
      translations.put(attrName, target);
    }
    target.put(orig, translation);
  }

}