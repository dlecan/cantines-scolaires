import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintWriter

import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.{LocationTextExtractionStrategy, PdfReaderContentParser, SimpleTextExtractionStrategy, TextExtractionStrategy}

object ExtractPageContent {

  def parsePdf(pdf: String) = {
    val reader = new PdfReader(pdf)
    val parser = new PdfReaderContentParser(reader)
    val out = new PrintWriter(System.out)

    println("Number of pages found: " + reader.getNumberOfPages)

    (1 to reader.getNumberOfPages).foreach {
      i =>
        println(s"Page number $i")
        val strategy = parser.processContent(i, new LocationTextExtractionStrategy)
        out.println(strategy.getResultantText())
    }
    out.flush()
    out.close()
  }

  def main(args: Array[String]) = {
    ExtractPageContent.parsePdf(getClass.getResource("/menu.pdf").toExternalForm)
  }
}