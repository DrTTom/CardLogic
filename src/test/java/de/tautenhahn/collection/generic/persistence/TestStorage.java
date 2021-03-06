package de.tautenhahn.collection.generic.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

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

    String protType = "protocol";
    String urlType = "cryptoUrl";
    String https = "https";
    WorkspacePersistence storingSysUnderTest = new WorkspacePersistence();
    storingSysUnderTest.init("testStoreAndFind");
    DescribedObject obj = new DescribedObject(urlType, "primary");
    obj.getAttributes().put(protType, https);
    DescribedObject prot = new DescribedObject(protType, https);
    reference = storingSysUnderTest.createNewBinRef(https, protType, "bin");
    prot.getAttributes().put("image", reference);
    storingSysUnderTest.store(new ByteArrayInputStream(content), reference);
    storingSysUnderTest.store(obj);
    storingSysUnderTest.store(prot);

    Persistence systemUnderTest = new WorkspacePersistence();

    systemUnderTest.init("testStoreAndFind");
    assertThat(systemUnderTest.find(urlType, "primary").getAttributes().get(protType)).isEqualTo(https);
    assertThat(systemUnderTest.isReferenced(protType, https, urlType)).isTrue();
    try (InputStream insRes = systemUnderTest.find(reference))
    {
      assertThat(insRes.available()).isEqualTo(content.length);
    }
    assertThat(systemUnderTest.findByRestriction(urlType, Map.of(protType, https)).count()).isEqualTo(1);
    systemUnderTest.delete(urlType, "primary");
    assertThat(systemUnderTest.isReferenced(protType, https, urlType)).isFalse();
    assertThat(systemUnderTest.find(urlType, "primary")).isNull();

  }

  /**
   * Storage should enable different collections and transfer data in and out. Look at the exported file to
   * make sure it is correct.
   *
   * @throws IOException
   */
  @Test
  public void importAndExport() throws IOException
  {
    importZip();
    WorkspacePersistence systemUnderTest = new WorkspacePersistence();
    try (OutputStream outs = new FileOutputStream("build/exportedByTest.zip"))
    {
      systemUnderTest.init("storageTest");
      assertThat(systemUnderTest.toString()).contains("storageTest, loaded 5 types");
      assertThat(systemUnderTest.getKeyValues("deck")).hasSize(99);
      systemUnderTest.exportZip(outs, x -> true);
    }
  }

  private void importZip() throws IOException
  {
    WorkspacePersistence systemUnderTest = new WorkspacePersistence();
    try (InputStream ins = TestStorage.class.getResourceAsStream("/example.zip"))
    {
      systemUnderTest.init("storageTest");
      systemUnderTest.importZip(ins);
    }
  }
}
