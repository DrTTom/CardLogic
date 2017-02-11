package de.tautenhahn.collection.generic.process;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

import de.tautenhahn.collection.cards.CardApplicationContext;
import de.tautenhahn.collection.process.ProcessScheduler;
import de.tautenhahn.collection.process.SearchProcess;

public class TestSearchProcess {

	@Test
	public void initialStep() {
		CardApplicationContext.init();
		SearchProcess search = ProcessScheduler.getInstance().getCurrentSearch("deck");
		assertThat(search.execute().getQuestions(), not(empty()));
		
	}

}
