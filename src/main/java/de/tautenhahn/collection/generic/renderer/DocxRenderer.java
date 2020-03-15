package de.tautenhahn.collection.generic.renderer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import de.tautenhahn.easydata.AccessibleData;
import de.tautenhahn.easydata.DataIntoTemplate;
import de.tautenhahn.easydata.docx.DocxAdapter;

/**
 * Creates output in DOCX format.
 *
 * @author ttautenhahn
 */
public abstract class DocxRenderer<T> implements DataRenderer<T>
{

    @Override
    public void render(List<T> input, OutputStream target) throws IOException
    {
        AccessibleData data = AccessibleData.byBean(Map.of("data", input));

        DocxAdapter adapter = new DocxAdapter(new DataIntoTemplate(data, '(', '@', ')'));

        try (InputStream ins = getTemplate())
        {
            adapter.convert(ins, target);
        }
    }

    /**
     * Provides the EasyData template
     *
     * @return template matching the data
     */
    protected abstract InputStream getTemplate();

    @Override
    public String getMediaType()
    {
        return "application/docx";
    }
}

