package de.tautenhahn.collection.generic.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
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
    try (WorkspacePersistence systemUnderTest = new WorkspacePersistence())
    {
      systemUnderTest.init("testStoreAndFind");
      DescribedObject obj = new DescribedObject(urlType, "primary");
      obj.getAttributes().put(protType, https);
      DescribedObject prot = new DescribedObject(protType, https);
      reference = systemUnderTest.createNewBinRef(https, protType, "bin");
      prot.getAttributes().put("image", reference);
      systemUnderTest.store(new ByteArrayInputStream(content), reference);
      systemUnderTest.store(obj);
      systemUnderTest.store(prot);
    }

    try (WorkspacePersistence systemUnderTest = new WorkspacePersistence())
    {
      systemUnderTest.init("testStoreAndFind");
      assertThat(systemUnderTest.find(urlType, "primary")
                                .getAttributes()
                                .get(protType)).isEqualTo(https);
      assertThat(systemUnderTest.isReferenced(protType, https, urlType)).isTrue();
      try (InputStream ins = systemUnderTest.find(reference))
      {
        assertThat(ins.available()).isEqualTo(content.length);
      }
      assertThat(systemUnderTest.findByRestriction(urlType, Map.of(protType, https))
                                .count()).isEqualTo(1);
      systemUnderTest.delete(urlType, "primary");
      assertThat(systemUnderTest.isReferenced(protType, https, urlType)).isFalse();
      assertThat(systemUnderTest.find(urlType, "primary")).isNull();
    }
  }

  /**
   * Storage should enable different collections and transfer data in and out.
   *
   * @throws IOException
   */
  @Test
  public void importAndExport() throws IOException
  {
    importZip();
    WorkspacePersistence systemUnderTest = new WorkspacePersistence();
    systemUnderTest.init("cards");
    assertThat(systemUnderTest.toString()).contains("cards, loaded 5 types");
    assertThat(systemUnderTest.getKeyValues("deck")).hasSize(99);
    Collection<String> binAttrs = Collections.singletonList("image");
    Map<String, Collection<String>> refs = Map.of("deck",
                                                  binAttrs,
                                                  "makerSign",
                                                  binAttrs,
                                                  "pattern",
                                                  binAttrs,
                                                  "taxStamp",
                                                  binAttrs);
    try (OutputStream outs = new FileOutputStream("build/exportedByTest.zip"))
    {
      systemUnderTest.exportZip(refs, outs);
    }
  }

  /**
   * Imports zip data into workspace:
   *
   * @throws IOException
   */
  void importZip() throws IOException
  {
    WorkspacePersistence systemUnderTest = new WorkspacePersistence();
    systemUnderTest.init("cards");
    try (InputStream ins = TestStorage.class.getResourceAsStream("/example.zip"))
    {
      systemUnderTest.importZip(ins);
    }
    systemUnderTest.close();
  }
}
