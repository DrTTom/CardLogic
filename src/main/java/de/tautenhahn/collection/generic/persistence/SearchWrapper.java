package de.tautenhahn.collection.generic.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSLockFactory;
import org.apache.lucene.store.SimpleFSDirectory;

import de.tautenhahn.collection.generic.ApplicationContext;
import de.tautenhahn.collection.generic.data.AttributeInterpreter;
import de.tautenhahn.collection.generic.data.DescribedObject;
import de.tautenhahn.collection.generic.data.DescribedObjectInterpreter;


/**
 * Calls the search and search indexing. This object works only for one primary object type.
 *
 * @author TT
 */
public class SearchWrapper
{

  private final Directory index;

  private final Analyzer analyzer;

  /**
   * Creates a new instance which works in the specified directory. If there is no index in that directory, it
   * creates one in a separate thread. Searching will block until the index is created.
   *
   * @param directory
   * @throws IOException
   */
  public SearchWrapper(Path directory) throws IOException
  {
    if (!Files.isDirectory(directory))
    {
      Files.createDirectories(directory);
    }
    index = new SimpleFSDirectory(directory, FSLockFactory.getDefault());
    analyzer = new StandardAnalyzer();
  }

  /**
   * Returns the primary keys of found objects.
   *
   * @param phrase
   * @param field
   * @throws IOException
   */
  public List<String> search(String phrase, String field) throws IOException
  {
    try (DirectoryReader reader = DirectoryReader.open(index))
    {
      Term term = new Term(field, phrase);
      WildcardQuery q = new WildcardQuery(term);
      IndexSearcher searcher = new IndexSearcher(reader);
      TopDocs searchResult = searcher.search(q, 100);
      List<String> result = new ArrayList<>();
      for ( ScoreDoc sdoc : searchResult.scoreDocs )
      {

        Document doc = searcher.doc(sdoc.doc);
        Optional.ofNullable(doc.getField("key")).map(IndexableField::stringValue).ifPresent(result::add);
      }
      return result;
    }
  }

  /**
   * Adds a given object to the search index.
   *
   * @param obj
   * @throws IOException
   */
  public void addToIndex(DescribedObject... obj) throws IOException
  {
    try (IndexWriter writer = getWriter())
    {
      for ( DescribedObject element : obj )
      {
        Document doc = buildDocument(element);
        writer.addDocument(doc);
      }
      writer.commit();
    }
  }

  /**
   * Removes the record specified by key from the index.
   *
   * @param key
   */
  public void removeFromIndex(String key)
  {
    // TODO - until implemented search may temporarily return wrong results.
  }

  private IndexWriter getWriter() throws IOException
  {
    IndexWriterConfig conf = new IndexWriterConfig(analyzer);
    return new IndexWriter(index, conf);
  }

  private Document buildDocument(DescribedObject obj)
  {
    DescribedObjectInterpreter interpreter = ApplicationContext.getInstance().getInterpreter(obj.getType());
    Document result = new Document();
    result.add(new TextField("key", obj.getPrimKey(), Store.YES));
    for ( String attributeName : interpreter.getSupportedAttributes() )
    {
      String value = obj.getAttributes().get(attributeName);
      if (value == null || value.chars().allMatch(Character::isWhitespace))
      {
        continue;
      }
      AttributeInterpreter ai = interpreter.getAttributeInterpreter(attributeName);
      if (ai.isSearchable())
      {
        result.add(new TextField(attributeName, value, Store.NO));
      }
    }
    return result;
  }
}
