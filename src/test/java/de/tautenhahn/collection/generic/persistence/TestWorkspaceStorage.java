package de.tautenhahn.collection.generic.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    String primaryType = "cryptoUrl";
    String auxType = "protocol";
    WorkspacePersistence systemUnderTest = new NoInterpreterPersistence();

    systemUnderTest.init("testing");
    DescribedObject obj = new DescribedObject(primaryType, "primary");
    obj.getAttributes().put(auxType, "https");
    DescribedObject prot = new DescribedObject(auxType, "https");
    reference = systemUnderTest.createNewBinRef("https", auxType, "bin");
    prot.getAttributes().put("image", reference);
    systemUnderTest.store(new ByteArrayInputStream(content), reference);
    systemUnderTest.store(obj);
    systemUnderTest.store(prot);
    try (FileOutputStream outs = new FileOutputStream("build/checkme.zip"))
    {
      systemUnderTest.exportZip(outs, x -> true);
    }

    WorkspacePersistence systemUnderTest2 = new WorkspacePersistence();
    {
      systemUnderTest2.init("testingOther");
      try (FileInputStream ins = new FileInputStream("build/checkme.zip"))
      {
        systemUnderTest2.importZip(ins);
      }
      assertThat(systemUnderTest2.find(primaryType, "primary")
                                 .getAttributes()
                                 .get(auxType)).isEqualTo("https");
      assertThat(systemUnderTest2.isReferenced(auxType, "https", primaryType)).isTrue();
      try (InputStream insRes = systemUnderTest2.find(reference))
      {
        assertThat(insRes.available()).isEqualTo(content.length);
      }
      systemUnderTest2.delete(primaryType, "primary");
      assertThat(systemUnderTest2.find(primaryType, "primary")).isNull();
    }
  }

  /**
   * Testable dummy class which does not rely on defined interpreters.
   */
  static class NoInterpreterPersistence extends WorkspacePersistence
  {

    @Override
    Map<String, Collection<String>> getKeysForExportableEntities()
    {
      return Map.of("cryptoUrl", Collections.emptyList(), "protocol", List.of("image"));
    }
  }
}
