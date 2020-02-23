package de.tautenhahn.collection.cards.labels;

import java.io.OutputStream;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


/**
 * This is just how you could implement a renderer. It cannot become part of the project due to license problems.
 * TODO: move to separate project (separate license, separate deployment ...) or replace by some free library
 * 
 * @author t.tautenhahn
 */
public class PdfRenderer implements LabelRenderer
{

  private static final Font BOLD = new Font(FontFamily.HELVETICA, 9, Font.BOLD);

  private static final Font NORMAL = new Font(FontFamily.HELVETICA, 9);

  private static final float LABEL_WIDTH = 159f;// 55mm

  @Override
  public void render(List<Label> labels, OutputStream target) throws DocumentException
  {
    Document doc = new Document();
    PdfWriter docWriter = PdfWriter.getInstance(doc, target);
    doc.open();

    int labelsPerLine = 3;

    PdfPTable table = new PdfPTable(labelsPerLine);
    table.setLockedWidth(true);
    table.setTotalWidth((LABEL_WIDTH + 4) * labelsPerLine);
    for ( Label label : labels )
    {
      PdfPCell cell = new PdfPCell(renderLabel(label));
      cell.setBorder(0);
      cell.setPadding(2f);
      table.addCell(cell);
    }
    for ( int i = 0 ; i < labels.size() % labelsPerLine ; i++ )
    {
      PdfPCell cell = new PdfPCell(new Phrase(""));
      cell.setBorder(0);
      table.addCell(cell);
    }
    doc.add(table);
    doc.close();
    docWriter.close();
  }

  private static PdfPTable renderLabel(Label label)
  {
    PdfPTable result = new PdfPTable(1);
    result.setLockedWidth(true);
    result.setTotalWidth(LABEL_WIDTH);
    PdfPCell cell = new PdfPCell();
    cell.setPadding(3f);
    cell.setPaddingTop(0f);
    cell.setFixedHeight(70f);

    Paragraph header = new Paragraph(label.header);
    header.setFont(BOLD);
    header.setMultipliedLeading(1.2f);
    cell.addElement(header);

    label.lines.forEach(l -> {
      Paragraph remark = new Paragraph(l);
      remark.setFont(NORMAL);
      remark.setSpacingBefore(2f);
      remark.setMultipliedLeading(1.2f);
      cell.addElement(remark);
    });
    result.addCell(cell);
    return result;
  }
}

