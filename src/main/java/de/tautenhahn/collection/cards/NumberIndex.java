package de.tautenhahn.collection.cards;

import de.tautenhahn.collection.generic.data.AttributeInterpreter;
import de.tautenhahn.collection.generic.data.Question;

public class NumberIndex extends AttributeInterpreter {

	protected NumberIndex() {
		super("numberIndex", Flag.EXACT);
	}

	@Override
	public boolean isLegalValue(String value) {
		return value.matches("[0-9](/[0-9])*");
	}

	@Override
	protected int correllateValue(String thisValue, String otherValue) {
		return thisValue.equals(otherValue)? 30: (thisValue.endsWith(otherValue) || otherValue.endsWith(thisValue))? 1: -1;
	}

	@Override
	public Question getQuestion() {
		Question result = new Question("numIndex", "Wie oft kommt der Index auf jeder Karte vor?", "Messen und zählen");
		result.setHelptext("Number of indexes on one card, Tarots big arcana and jokers excluded."
				+"If number differs on cards (not counting missing index as described by index property),"+
				"\nwrite a/b starting with smallest rank. For instance, if a deck has 2 indices on court cards"+
				"\nand 4 on other cards, write '4/2', if index attribute is '78910', each number occurring "+
				"\ntwice on a card, write '2' instead of '2/0'.");
		return result;
	}

}
