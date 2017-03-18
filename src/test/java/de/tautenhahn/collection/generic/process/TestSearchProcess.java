package de.tautenhahn.collection.generic.process;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.Collections;

import org.junit.Test;

import de.tautenhahn.collection.cards.CardApplicationContext;
import de.tautenhahn.collection.process.ProcessScheduler;
import de.tautenhahn.collection.process.SearchProcess;


public class TestSearchProcess
{

  @Test
  public void initialStep()
  {
    CardApplicationContext.register("cards");
    SearchProcess search = ProcessScheduler.getInstance().getCurrentSearch("deck");
    assertThat(search.execute(Collections.emptyMap()).getQuestions(), not(empty()));
  }

}
