package de.tautenhahn.collection.generic.persistence;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Ignore;
import org.junit.Test;

import de.tautenhahn.collection.generic.data.DescribedObject;


public class RestructureStorage
{

  Properties makerKeys = new Properties();

  Properties patternKeys = new Properties();

  @Test
  @Ignore("this is a tool")
  public void test() throws IOException
  {
    Persistence source = new WorkspacePersistence();
    source.init("cards");
    WorkspacePersistence dest = new WorkspacePersistence();
    dest.init("cards_new");
    try (Reader reader = new InputStreamReader(getClass().getResourceAsStream("makerMapping"),
                                               StandardCharsets.UTF_8))
    {
      makerKeys.load(reader);
    }

    migratePatterns(source, dest);

    migrateMaker(source, dest);

    migrateTaxStamp(source, dest);

    migrateMakerSign(source, dest);

    for ( String key : source.getKeyValues("deck") )
    {
      if (key.length() > 2)
        continue;
      DescribedObject deck = source.find("deck", key);

      if (deck.getAttributes().get("maker") != null)
      {
        deck.getAttributes().put("maker", keyFor(deck.getAttributes().get("maker"), makerKeys));
      }
      if (deck.getAttributes().get("makerSign") != null)
      {
        deck.getAttributes().put("makerSign", toMakerSignKey(deck.getAttributes().get("makerSign")));
      }
      if (deck.getAttributes().get("pattern") != null)
      {
        deck.getAttributes().put("pattern", keyFor(deck.getAttributes().get("pattern"), patternKeys));
      }
      if (deck.getAttributes().get("taxStamp") != null)
      {
        deck.getAttributes().put("taxStamp", toTaxStampKey(deck.getAttributes().get("taxStamp")));
      }

      if (deck.getAttributes().get("image") != null)
      {
        try (InputStream ins = source.find(deck.getAttributes().get("image")))
        {
          String ref = dest.createNewBinRef(deck.getPrimKey(), "deck", "jpg");
          dest.store(ins, ref);
          deck.getAttributes().put("image", ref);
        }
      }
      dest.store(deck);
    }
    System.out.println(dest.getKeyValues("deck").size() + " decks migrated of "
                       + source.getKeyValues("deck").size());
    System.out.println(dest.getKeyValues("maker").size() + " makers migrated of "
                       + source.getKeyValues("maker").size());
    dest.close();

    try (OutputStream out = new FileOutputStream("example.zip"))
    {
      Map<String, List<String>> binRefs = new HashMap<>();
      binRefs.put("deck", Collections.singletonList("image"));
      binRefs.put("makerSign", Collections.singletonList("image"));
      binRefs.put("pattern", Collections.singletonList("image"));
      binRefs.put("taxStamp", Collections.singletonList("image"));
      dest.exportZip(binRefs, out);
    }
  }

  private void migrateTaxStamp(Persistence source, Persistence dest) throws IOException
  {
    for ( String key : source.getKeyValues("taxStamp") )
    {
      DescribedObject taxStamp = source.find("taxStamp", key);
      String authority = key.startsWith("Deu") ? "Deutsches Reich" : "Österreich";
      taxStamp.getAttributes().put("issuer", authority);
      taxStamp = new DescribedObject(taxStamp.getType(), toTaxStampKey(key),
                                     new HashMap<>(taxStamp.getAttributes()));
      taxStamp.getAttributes().put("name",
                                   authority + " " + taxStamp.getAttributes().get("from") + "-"
                                           + taxStamp.getAttributes().get("to"));

      try (InputStream ins = source.find(taxStamp.getAttributes().get("image")))
      {
        String ref = dest.createNewBinRef(taxStamp.getPrimKey(), "taxStamp", "jpg");
        dest.store(ins, ref);
        taxStamp.getAttributes().put("image", ref);
      }

      dest.store(taxStamp);

    }
  }

  private void migrateMakerSign(Persistence source, Persistence dest) throws IOException
  {
    for ( String key : source.getKeyValues("makerSign") )
    {
      DescribedObject makerSign = source.find("makerSign", key);
      makerSign.getAttributes().put("name", key);
      if (makerSign.getAttributes().get("maker") != null)
      {
        makerSign.getAttributes().put("maker", keyFor(makerSign.getAttributes().get("maker"), makerKeys));
      }

      makerSign = new DescribedObject(makerSign.getType(), toMakerSignKey(key),
                                      new HashMap<>(makerSign.getAttributes()));
      if (makerSign.getAttributes().get("image") != null)
      {
        try (InputStream ins = source.find(makerSign.getAttributes().get("image")))
        {
          String ref = dest.createNewBinRef(makerSign.getPrimKey(), "makerSign", "jpg");
          dest.store(ins, ref);
          makerSign.getAttributes().put("image", ref);
        }
        dest.store(makerSign);
      }
    }
  }

  private String toMakerSignKey(String key)
  {
    return key.equals("Nürnberger") ? "nuernb" : key;
  }

  private String toTaxStampKey(String key)
  {
    return key.startsWith("Deu") ? "DE" + key.substring(16, 20) : "AU1900";

  }

  private void migrateMaker(Persistence source, Persistence dest) throws IOException
  {
    for ( String key : source.getKeyValues("maker") )
    {
      DescribedObject maker = source.find("maker", key);
      String name = maker.getAttributes().remove("fullName");
      if (name == null)
      {
        name = key;
      }
      maker.getAttributes().put("name", name);
      maker = new DescribedObject(maker.getType(), keyFor(key, makerKeys),
                                  new HashMap<>(maker.getAttributes()));
      dest.store(maker);
    }
  }

  private void migratePatterns(Persistence source, Persistence dest) throws IOException
  {
    try (Reader reader = new InputStreamReader(getClass().getResourceAsStream("patternMapping"),
                                               StandardCharsets.UTF_8))
    {
      patternKeys.load(reader);
    }
    for ( String key : source.getKeyValues("pattern") )
    {
      DescribedObject pattern = source.find("pattern", key);
      pattern.getAttributes().put(DescribedObject.NAME_KEY, pattern.getPrimKey());
      pattern = new DescribedObject(pattern.getType(), keyFor(pattern.getPrimKey(), patternKeys),
                                    new HashMap<>(pattern.getAttributes()));
      String imgRef = pattern.getAttributes().get("image");
      if (imgRef != null)
      {
        String ref = dest.createNewBinRef(pattern.getPrimKey(), "pattern", "jpg");
        try (InputStream ins = source.find(imgRef))
        {
          dest.store(ins, ref);
        }
        pattern.getAttributes().put("image", ref);
      }
      dest.store(pattern);

    }
  }

  private String keyFor(String primKey, Properties patternKeys)
  {
    return (String)patternKeys.entrySet()
                              .stream()
                              .filter(e -> e.getValue().equals(primKey))
                              .findAny()
                              .map(e -> e.getKey())
                              .orElseThrow(() -> new RuntimeException());

  }

}
