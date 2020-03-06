package de.tautenhahn.collection.generic.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.tautenhahn.collection.generic.ApplicationContext;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import de.tautenhahn.collection.generic.data.DescribedObject;


/**
 * Unit tests for the work space storage.
 *
 * @author TT
 */
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
    byte[] content = new byte[1001];
    String reference = null;
    try (WorkspacePersistence systemUnderTest = new NoInterpreterPersistence())
    {
      systemUnderTest.init("testing");
      DescribedObject obj = new DescribedObject("cryptoUrl", "primary");
      obj.getAttributes().put("protocol", "https");
      DescribedObject prot = new DescribedObject("protocol", "https");
      reference = systemUnderTest.createNewBinRef("https", "protocol", "bin");
      prot.getAttributes().put("image", reference);
      systemUnderTest.store(new ByteArrayInputStream(content), reference);
      systemUnderTest.store(obj);
      systemUnderTest.store(prot);
      try (FileOutputStream outs = new FileOutputStream("build/checkme.zip"))
      {
        systemUnderTest.exportZip(outs, x->true);
      }
    }

    try (WorkspacePersistence systemUnderTest = new WorkspacePersistence())
    {
      systemUnderTest.init("testingOther");
      try (FileInputStream ins = new FileInputStream("build/checkme.zip"))
      {
        systemUnderTest.importZip(ins);
      }
      assertThat(systemUnderTest.find("cryptoUrl", "primary")
                                .getAttributes()
                                .get("protocol")).isEqualTo("https");
      assertThat(systemUnderTest.isReferenced("protocol", "https", "cryptoUrl")).isTrue();
      try (InputStream insRes = systemUnderTest.find(reference))
      {
        assertThat(insRes.available()).isEqualTo(content.length);
      }
      systemUnderTest.delete("cryptoUrl", "primary");
      assertThat(systemUnderTest.find("cryptoUrl", "primary")).isNull();
    }
  }

  /**
   * Imports zip data into workspace:
   *
   * @throws IOException
   */
  @Test
  @Disabled("this is a tool")
  public void importZip() throws IOException
  {
    try (WorkspacePersistence systemUnderTest = new WorkspacePersistence();
      InputStream ins = TestWorkspaceStorage.class.getResourceAsStream("/example.zip"))
    {
      systemUnderTest.init("cards");
      systemUnderTest.importZip(ins);
    }
  }

  static class NoInterpreterPersistence extends WorkspacePersistence
  {
    @Override
    Map<String, Collection<String>> getKeysForExportableEntities()
    {
      return Map.of("cryptoUrl", Collections.emptyList(), "protocol", List.of("image"));
    }
  }
}
