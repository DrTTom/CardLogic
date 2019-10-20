package de.tautenhahn.collection.generic.data;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.tautenhahn.collection.generic.persistence.Persistence;
import de.tautenhahn.collection.generic.persistence.WorkspacePersistence;


/**
 * Unit test for check logic in Year class.
 *
 * @author TT
 */
public class TestYear
{

  // do not call init or close in this test!
  private static Persistence persistence = new WorkspacePersistence();

  /**
   * Asserts that Year class produces errors when value violates defined restrictions.
   */
  @Test
  public void imposedBounds()
  {
    DescribedObject master = new DescribedObject("a", "b");
    master.getAttributes().put("maker", "makerA");
    master.getAttributes().put("invented", "1954");
    master.getAttributes().put("acquired", "1993");
    master.getAttributes().put("blind", "dummy");

    DescribedObject maker = new DescribedObject("maker", "makerA");
    maker.getAttributes().put("founded", "1961");
    persistence.store(maker);

    Year systemUnderTest = new Year("created");
    systemUnderTest.persistence = persistence;
    systemUnderTest.addNotBeforeRestriction("maker", "founded");
    systemUnderTest.addNotBeforeRestriction("invented");
    systemUnderTest.addNotAfterRestriction("acquired");
    systemUnderTest.addNotAfterRestriction("blind", "foo", "bar");
    systemUnderTest.addNotAfterRestriction("foo");

    assertThat("maker", systemUnderTest.check("1953", master), is("msg.error.tooEarlyFor.maker"));
    master.getAttributes().put("invented", "1962");
    assertThat("invented", systemUnderTest.check("1953", master), is("msg.error.tooEarlyFor.invented"));
    assertThat("after acquired", systemUnderTest.check("1999", master), is("msg.error.tooLateFor.acquired"));
  }
}
