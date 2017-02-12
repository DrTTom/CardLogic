package de.tautenhahn.collection.cards;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import de.tautenhahn.collection.generic.data.AttributeInterpreter;
import de.tautenhahn.collection.generic.data.DescribedObjectInterpreter;
import de.tautenhahn.collection.generic.data.Question;

public class Deck extends DescribedObjectInterpreter {

	private static final String TYPE = "Deck";

	private static final Map<String, AttributeInterpreter> ATTRIBS = new HashMap<>();
	static {
		ATTRIBS.put("suits", new Suits());
		ATTRIBS.put("condition", new Condition());
		ATTRIBS.put("numberCards", new NumberCards());
		ATTRIBS.put("numIndex", new NumberIndex());
	}

	@Override
	public Collection<String> getSupportedAttributes() {
		return ATTRIBS.keySet();
	}

	@Override
	public Collection<String> getBinaryValuedAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AttributeInterpreter getAttributeInterpreter(String name) {
		return Optional.ofNullable(ATTRIBS.get(name))
				.orElseThrow(() -> new IllegalArgumentException("unsupported attribute " + name));

	}

	@Override
	public Collection<Question> getQuestions() {
		return ATTRIBS.values().stream().map(i -> i.getQuestion()).collect(Collectors.toList());
	}

}
