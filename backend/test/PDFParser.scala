import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintWriter

import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.PdfReaderContentParser
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy
import com.itextpdf.text.pdf.parser.TextExtractionStrategy

object ExtractPageContent {

  def parsePdf(pdf: String, txt: String) = {
    val reader = new PdfReader(pdf)
    val parser = new PdfReaderContentParser(reader)
    val out = new PrintWriter(new FileOutputStream(txt))
//    for (int i = 1; i <= reader.getNumberOfPages(); i++) {
    (1 until reader.getNumberOfPages()).foreach {
      i =>
      val strategy = parser.processContent(i, new SimpleTextExtractionStrategy())
      out.println(strategy.getResultantText())
    }
    out.flush()
    out.close()
  }

  def main(args: Array[String]) = {
    new ExtractPageContent().parsePdf(PREFACE, RESULT)
  }
}