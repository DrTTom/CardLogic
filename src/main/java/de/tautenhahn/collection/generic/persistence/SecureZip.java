package de.tautenhahn.collection.generic.persistence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


/**
 * Wraps secure ZIP file handling.
 *
 * @author TT
 */
public class SecureZip
{

  private static final int MAX_FILESIZE = 100 * 1024 * 1024;

  /**
   * Creates a ZIP containing specified set of files.
   *
   * @param baseDir root of directory tree
   * @param relativePaths paths of files relative to baseDir
   * @param target stream to write to.
   * @throws IOException
   */
  public void create(Path baseDir, Collection<String> relativePaths, OutputStream target) throws IOException
  {
    try (ZipOutputStream zip = new ZipOutputStream(target))
    {
      for ( String path : relativePaths )
      {
        zip.putNextEntry(new ZipEntry(path));
        Files.copy(baseDir.resolve(path), zip);
        zip.flush();
      }
    }
  }

  /**
   * Unpacks a ZIP content
   *
   * @param source first entry should contain the description of whole content.
   * @param baseDir target base directory
   * @param structureEntryName expected name of the first entry, used to recognize file format.
   * @param c reads the structure description
   * @throws IOException
   */
  public void expand(InputStream source, Path baseDir, String structureEntryName, FirstEntryConsumer c)
    throws IOException
  {
    try (ZipInputStream zip = new ZipInputStream(source))
    {
      ZipEntry entry = zip.getNextEntry();
      if (!structureEntryName.equals(entry.getName()) || entry.isDirectory())
      {
        throw new IOException("Zip file is not a Collection -> aborting import without changes to workspace.");
      }
      try (InputStream nonClosing = new NonClosingFilter(zip);
        Reader reader = new InputStreamReader(nonClosing, StandardCharsets.UTF_8))
      {
        int allowedNumberFiles = c.read(reader) * 2;
        zip.closeEntry();
        entry = zip.getNextEntry();
        while (entry != null && allowedNumberFiles > 0)
        {
          createLimitedFile(baseDir, entry.getName(), zip);
          allowedNumberFiles--;
          zip.closeEntry();
          entry = zip.getNextEntry();
        }
      }
    }
  }

  private void createLimitedFile(Path baseDir, String relativePath, InputStream content) throws IOException
  {
    File target = baseDir.resolve(relativePath).toFile();
    if (target.getCanonicalPath().startsWith(baseDir.toFile().getCanonicalPath()))
    {
      if (!target.getParentFile().exists() && !target.getParentFile().mkdirs())
      {
        throw new IOException("Cannot create directory " + target.getParentFile().getAbsolutePath());
      }
      doWrite(content, target);
    }
  }

  private void doWrite(InputStream content, File target) throws IOException
  {
    try (FileOutputStream fos = new FileOutputStream(target))
    {
      byte[] buffer = new byte[4 * 1024];
      int total = 0;
      int count = 0;
      while (total + buffer.length <= MAX_FILESIZE)
      {
        count = content.read(buffer);
        if (count == -1)
        {
          break;
        }
        fos.write(buffer, 0, count);
        total += count;
      }
    }
  }

  /**
   * just naming the lambda
   */
  interface FirstEntryConsumer
  {

    /**
     * Reads the first ZIP entry (in our case the JSON file) and returns the number of expected further files.
     */
    int read(Reader reader) throws IOException;
  }

  /**
   * need to pass streams to consumers but may not close ZIP stream prematurely
   */
  private static class NonClosingFilter extends FilterInputStream
  {

    NonClosingFilter(InputStream ins)
    {
      super(ins);
    }

    @Override
    public void close()
    {
      // not closing on purpose
    }
  }
}
