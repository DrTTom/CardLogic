package de.tautenhahn.collection.generic.persistence;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import de.tautenhahn.collection.generic.data.DescribedObject;


public class TestWorkspaceStorage
{

  /**
   * Stores and finds a few objects.
   *
   * @throws IOException
   */
  @Test
  public void storeAndFind() throws IOException
  {
    Persistence systemUnderTest = new WorkspacePersistence();
    systemUnderTest.init();
    DescribedObject obj = new DescribedObject("cryptoUrl", "primary");
    obj.getAttributes().put("protocol", "https");
    DescribedObject prot = new DescribedObject("protocol", "https");
    byte[] content = new byte[1001];
    String reference = systemUnderTest.createNewBinRef("https", "protocol", "bin");
    systemUnderTest.store(new ByteArrayInputStream(content), reference);
    systemUnderTest.store(obj);
    systemUnderTest.store(prot);
    systemUnderTest.close();

    systemUnderTest = new WorkspacePersistence();
    systemUnderTest.init();
    assertThat(systemUnderTest.find("cryptoUrl", "primary").getAttributes().get("protocol"), is("https"));
    assertTrue(systemUnderTest.isReferenced("protocol", "https", "cryptoUrl"));
    try (InputStream ins = systemUnderTest.find(reference))
    {
      assertThat(ins.available(), is(content.length));
    }
  }

}
