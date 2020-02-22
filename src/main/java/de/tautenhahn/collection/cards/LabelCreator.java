package de.tautenhahn.collection.cards;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;



public class LabelCreator
{

  public static void main(String[] args) throws IOException, DocumentException
  {
    Document doc = new Document();
    try (OutputStream out = new FileOutputStream("checkme.pdf"))
    {
      PdfWriter docWriter = PdfWriter.getInstance(doc, out);
      doc.open();
      Paragraph paragraph = new Paragraph("Sollte wenigstens umbrechen können: iText ® is a library that allows you to create and "
                                          + "manipulate PDF documents. It enables developers looking to enhance web and other "
                                          + "applications with dynamic PDF document generation and/or manipulation.");
      doc.add(paragraph);
      doc.close();
      docWriter.close();
    }

    // try (PDDocument doc = new PDDocument())
    // {
    // PDPage page = new PDPage();
    // doc.addPage(page);
    // PDFont font = PDType0Font.load(doc,
    // LabelCreator.class.getResourceAsStream("/org/apache/pdfbox/resources/ttf/LiberationSans-Regular.ttf"));
    //
    // try (PDPageContentStream contents = new PDPageContentStream(doc, page))
    // {
    // contents.beginText();
    // contents.setFont(font, 12);
    // contents.newLineAtOffset(100, 700);
    // contents.showText("this is some text - no formatting, nothing fancy");
    // contents.endText();
    // }
    //
    //
    // doc.save("checkme.pdf");
    //
    // }
  }
}

