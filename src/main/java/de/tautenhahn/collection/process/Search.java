package de.tautenhahn.collection.process;

import java.util.ArrayList;
import java.util.List;

import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.Message;
import de.tautenhahn.collection.generic.data.Question;

/**
 * Wraps all data about the search process transported to the front end.  
 * @author TT
 *
 */
public class Search {

	  private String type;

	  private String queryText;

	  private List<Question> questions;
	  
	  private int currentQuestion;
	  
	  private List<Message> messages;

	  private int numberTotal;

	  private List<DescribedObject> matches = new ArrayList<>();

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getQueryText() {
		return queryText;
	}

	public void setQueryText(String queryText) {
		this.queryText = queryText;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public int getCurrentQuestion() {
		return currentQuestion;
	}

	public void setCurrentQuestion(int currentQuestion) {
		this.currentQuestion = currentQuestion;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public int getNumberTotal() {
		return numberTotal;
	}

	public void setNumberTotal(int numberTotal) {
		this.numberTotal = numberTotal;
	}

	public List<DescribedObject> getMatches() {
		return matches;
	}

	public void setMatches(List<DescribedObject> matches) {
		this.matches = matches;
	}

}
