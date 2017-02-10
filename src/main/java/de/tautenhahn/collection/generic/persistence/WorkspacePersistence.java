package de.tautenhahn.collection.generic.persistence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import de.tautenhahn.collection.generic.data.DescribedObject;


/**
 * Persistence implementation which holds all the data within a directory tree. DescribedObjects are loaded
 * into memory, so use only for small collections!
 *
 * @author TT
 */
public class WorkspacePersistence implements Persistence
{

  private static final String JSON_FILENAME = "objects.json";

  private final Map<String, Map<String, DescribedObject>> objects = new TreeMap<>();

  private Path collectionBaseDir;

  @Override
  public void store(DescribedObject item)
  {
    getTypeMap(item.getType()).put(item.getPrimKey(), item);
  }

  @Override
  public DescribedObject find(String type, String primKey)
  {
    return Optional.ofNullable(objects.get(type)).map(m -> m.get(primKey)).orElse(null);
  }

  @Override
  public Stream<DescribedObject> findAll(String type)
  {
    return getTypeMap(type).values().stream();
  }

  @Override
  public Stream<DescribedObject> findByRestriction(String type, Map<String, String> exactValues)
  {
    return getTypeMap(type).values()
                           .stream()
                           .filter(d -> exactValues.entrySet()
                                                   .stream()
                                                   .allMatch(ev -> Objects.equals(d.getAttributes()
                                                                                   .get(ev.getKey()),
                                                                                  ev.getValue())));
  }

  @Override
  public void close() throws IOException
  {
    Gson gson = new GsonBuilder().create();

    Path path = collectionBaseDir.resolve(JSON_FILENAME);
    try (Writer wr = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
      JsonWriter writer = new JsonWriter(wr))
    {
      writer.setIndent(" ");
      writer.beginArray();
      objects.values().forEach(m -> m.values().forEach(d -> gson.toJson(d, DescribedObject.class, writer)));
      writer.endArray();
    }
  }

  @Override
  public void init(String... args) throws IOException
  {
    String collectionName = args.length > 0 ? args[0] : "default";
    collectionBaseDir = Paths.get(System.getProperty("user.home"), ".Collection", collectionName);
    if (!Files.exists(collectionBaseDir))
    {
      Files.createDirectories(collectionBaseDir);
    }
    Path path = collectionBaseDir.resolve(JSON_FILENAME);
    if (Files.exists(path))
    {
      try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8))
      {
        importGson(reader);
      }
    }
  }

  private int importGson(Reader reader) throws IOException
  {
    int result = 0;
    Gson gson = new GsonBuilder().create();
    try (JsonReader jr = gson.newJsonReader(reader))
    {
      jr.beginArray();
      while (jr.hasNext())
      {
        store(gson.fromJson(jr, DescribedObject.class));
        result++;
      }
      jr.endArray();
    }
    return result;
  }

  /**
   * Returns a path for a new binary resource, makes sure no directory becomes too full.
   *
   * @throws IOException
   */
  @Override
  public String createNewBinRef(String parentsPrimKey, String parentsType, String fileExtension)
    throws IOException
  {
    Path subDir = collectionBaseDir.resolve(Paths.get(sanitize(parentsType),
                                                      Integer.toHexString(parentsPrimKey.hashCode() % 64)));
    if (!Files.exists(subDir))
    {
      Files.createDirectories(subDir);
    }
    Path result = subDir.resolve(sanitize(parentsPrimKey) + "." + fileExtension);
    int diff = 0;
    while (Files.exists(result))
    {
      result = subDir.resolve(sanitize(parentsPrimKey) + "_" + (diff++) + "." + fileExtension);
    }
    return collectionBaseDir.relativize(result).toString();
  }

  private String sanitize(String input)
  {
    StringBuilder result = new StringBuilder();
    for ( char x : input.toCharArray() )
    {
      if (Character.isLetterOrDigit(x))
      {
        result.append(x);
      }
      else
      {
        result.append("_").append((int)x);
      }
    }
    return result.toString();
  }

  @Override
  public int getNumberItems(String type)
  {
    return getTypeMap(type).size();
  }

  @Override
  public List<String> getKeyValues(String type)
  {
    ArrayList<String> result = new ArrayList<>(getTypeMap(type).keySet());
    Collections.sort(result);
    return result;
  }

  @Override
  public void delete(String type, String name)
  {
    getTypeMap(type).remove(name);

  }

  @Override
  public boolean isReferenced(String type, String name, String... referencingType)
  {
    for ( String refType : referencingType )
    {
      if (findAll(refType).anyMatch(d -> name.equals(d.getAttributes().get(type))))
      {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean keyExists(String type, String name)
  {
    return getTypeMap(type).containsKey(name);
  }

  @Override
  public List<String> getObjectTypes()
  {
    return new ArrayList<>(objects.keySet());
  }

  @Override
  public void store(InputStream ins, String ref) throws IOException
  {
    Files.copy(ins, collectionBaseDir.resolve(ref));
  }

  @Override
  public InputStream find(String ref) throws IOException
  {
    return Files.newInputStream(collectionBaseDir.resolve(ref));
  }

  @Override
  public String search(String type, String query)
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void addToIndex(String type, String key, String data)
  {
    // TODO Auto-generated method stub

  }

  private Map<String, DescribedObject> getTypeMap(String type)
  {
    Map<String, DescribedObject> result = objects.get(type);
    if (result == null)
    {
      result = new TreeMap<>();
      objects.put(type, result);
    }
    return result;
  }

  public void exportZip(Map<String, List<String>> binRefs, OutputStream outs) throws IOException
  {
    close();
    List<String> relPathes = new ArrayList<>();
    relPathes.add(JSON_FILENAME);
    binRefs.forEach((type, binAttrs) -> {
      getTypeMap(type).values()
                      .stream()
                      .flatMap(v -> v.getAttributes().entrySet().stream())
                      .filter(e -> binAttrs.contains(e.getKey()))
                      .map(e -> e.getValue())
                      .filter(p -> p != null)
                      .forEach(relPathes::add);
    });

    try (ZipOutputStream zip = new ZipOutputStream(outs))
    {
      for ( String path : relPathes )
      {
        zip.putNextEntry(new ZipEntry(path));
        Files.copy(collectionBaseDir.resolve(path), zip);
        zip.flush();
      }
    }
  }

  private static class NonClosingFilter extends InputStream
  {

    private final InputStream ins;

    NonClosingFilter(InputStream ins)
    {
      this.ins = ins;
    }

    @Override
    public int read() throws IOException
    {
      return ins.read();
    }

  }

  public void importZip(InputStream ins) throws IOException
  {
    try (ZipInputStream zip = new ZipInputStream(ins))
    {
      ZipEntry entry = zip.getNextEntry();
      if (!JSON_FILENAME.equals(entry.getName()) || entry.isDirectory())
      {
        throw new IOException("Zip file is not a Collection -> aborting import without changes to wiorkspace.");
      }
      int allowedNumberFiles = 0;
      try (InputStream nonClosing = new NonClosingFilter(zip);
        Reader reader = new InputStreamReader(nonClosing, StandardCharsets.UTF_8))
      {
        allowedNumberFiles = importGson(reader) * 10;
        close();
        zip.closeEntry();
        int numberFiles = 1;
        while ((entry = zip.getNextEntry()) != null && numberFiles < allowedNumberFiles)
        {
          createLimitedFile(entry.getName(), zip);
          zip.closeEntry();
        }
      }
    }
  }

  private static final int MAX_FILESIZE = 100 * 1024 * 1024;

  private void createLimitedFile(String relativePath, InputStream content) throws IOException
  {
    File target = collectionBaseDir.resolve(relativePath).toFile();
    if (target.getCanonicalPath().startsWith(collectionBaseDir.toFile().getCanonicalPath()))
    {
      if (!target.getParentFile().exists())
      {
        target.getParentFile().mkdirs();
      }
      try (OutputStream fos = new FileOutputStream(target))
      {
        byte[] buffer = new byte[4 * 1024];
        int total = 0;
        int count = 0;

        while (total + buffer.length <= MAX_FILESIZE && (count = content.read(buffer)) != -1)
        {
          fos.write(buffer, 0, count);
          total += count;
        }
      }
    }
  }
}
