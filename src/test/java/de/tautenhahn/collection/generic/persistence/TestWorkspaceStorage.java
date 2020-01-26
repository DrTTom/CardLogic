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
import java.util.Map;

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
    try (WorkspacePersistence systemUnderTest = new WorkspacePersistence();)
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
        systemUnderTest.exportZip(Collections.singletonMap("protocol", Collections.singletonList("image")),
                                  outs);
      }
    }

    try (WorkspacePersistence systemUnderTest = new WorkspacePersistence();)
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
      try (InputStream ins = systemUnderTest.find(reference))
      {
        assertThat(ins.available()).isEqualTo(content.length);
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

  /**
   * Sort migrated data into new workspace
   *
   * @throws IOException
   */
  @Test
  @Disabled
  public void read() throws IOException
  {
    WorkspacePersistence systemUnderTest = new WorkspacePersistence();
    systemUnderTest.init("cards");
    systemUnderTest.close();
    systemUnderTest.getObjectTypes()
                   .forEach(t -> systemUnderTest.getKeyValues(t)
                                                .forEach(k -> importImage(t, k, systemUnderTest)));
    systemUnderTest.close();
    try (OutputStream out = new FileOutputStream("example.zip"))
    {
      Map<String, Collection<String>> binRefs = new HashMap<>();
      binRefs.put("deck", Collections.singletonList("image"));
      binRefs.put("makerSign", Collections.singletonList("image"));
      binRefs.put("pattern", Collections.singletonList("image"));
      binRefs.put("taxStamp", Collections.singletonList("image"));
      systemUnderTest.exportZip(binRefs, out);
    }
  }

  private void importImage(String type, String key, WorkspacePersistence systemUnderTest)
  {
    try
    {
      DescribedObject object = systemUnderTest.find(type, key);
      String imageRef = object.getAttributes().get("image");
      if (imageRef != null)
      {
        if ("null".equals(imageRef))
        {
          object.getAttributes().remove("image");
          return;
        }
        String newRef = systemUnderTest.createNewBinRef(key, type, "jpg");
        object.getAttributes().put("image", newRef);
        try (InputStream ins = getClass().getResourceAsStream(imageRef.startsWith("/") ? imageRef
          : "/" + imageRef))
        {
          System.out.println(imageRef + " -> " + ins);
          if (ins == null)
          {
            object.getAttributes().remove("image");
            return;
          }
          systemUnderTest.store(ins, newRef);
        }
      }
    }
    catch (IOException e)
    {
      fail(e);
    }
  }
}
