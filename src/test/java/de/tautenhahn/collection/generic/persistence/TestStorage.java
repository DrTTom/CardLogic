package de.tautenhahn.collection.generic.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

import de.tautenhahn.collection.generic.data.DescribedObject;


/**
 * Unit tests for storage implementation.
 *
 * @author TT
 */
public class TestStorage
{

  /**
   * Storage must be able to store and find objects.
   *
   * @throws IOException
   */
  @Test
  public void storeAndFind() throws IOException
  {
    byte[] content = new byte[1001];
    String reference = null;

    try (WorkspacePersistence systemUnderTest = new WorkspacePersistence())
    {
      systemUnderTest.init("testStoreAndFind");
      DescribedObject obj = new DescribedObject("cryptoUrl", "primary");
      obj.getAttributes().put("protocol", "https");
      DescribedObject prot = new DescribedObject("protocol", "https");
      reference = systemUnderTest.createNewBinRef("https", "protocol", "bin");
      prot.getAttributes().put("image", reference);
      systemUnderTest.store(new ByteArrayInputStream(content), reference);
      systemUnderTest.store(obj);
      systemUnderTest.store(prot);
    }


    try (WorkspacePersistence systemUnderTest = new WorkspacePersistence())
    {
      systemUnderTest.init("testStoreAndFind");
      assertThat(systemUnderTest.find("cryptoUrl", "primary")
                                .getAttributes()
                                .get("protocol")).isEqualTo("https");
      assertThat(systemUnderTest.isReferenced("protocol", "https", "cryptoUrl")).isTrue();
      try (InputStream ins = systemUnderTest.find(reference))
      {
        assertThat(ins.available()).isEqualTo(content.length);
      }
      systemUnderTest.delete("cryptoUrl", "primary");
      assertThat(systemUnderTest.find("cryptoUrl", "primary")).isNull();
    }
  }

  /**
   * Storage should enable different collections and transfer data in and out.
   *
   * @throws IOException
   */

  /**
   * Imports zip data into workspace:
   *
   * @throws IOException
   */
  public void importZip() throws IOException
  {
    WorkspacePersistence systemUnderTest = new WorkspacePersistence();
    systemUnderTest.init("cards");
    try (InputStream ins = TestStorage.class.getResourceAsStream("/example.zip"))
    {
      systemUnderTest.importZip(ins);
    }
  }


}
