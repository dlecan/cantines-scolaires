package parsers

import java.net.URL
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.{PdfTextExtractor, LocationTextExtractionStrategy, FilteredTextRenderListener, RegionTextRenderFilter}
import com.itextpdf.text.Rectangle

trait PdfParserComponent {
  val pdfParser: PdfParser
}

trait PdfParser {
  def parsePdf(url: URL): List[String]
}

trait ITextPdfParserComponent extends PdfParserComponent {

  override val pdfParser: PdfParser = new ITextPdfParser

  object ITextPdfParser {
    val xOffset = 80
    val yOffset = 16

    val rowWidth = 160
    val colHeight = 121
  }

  class ITextPdfParser extends PdfParser {

    import ITextPdfParser._

    override def parsePdf(url: URL): List[String] = {
      val reader = new PdfReader(url)

      val results = for {
        row <- 0 to 3
        col <- 0 to 4
      } yield {
        val rect = getRectangle(row, col)
        val filter = new RegionTextRenderFilter(rect)
        val strategy = new FilteredTextRenderListener(new LocationTextExtractionStrategy(), filter)

        PdfTextExtractor.getTextFromPage(reader, 1, strategy)
      }
      reader.close()
      results.toList
    }

    private def getRectangle(row: Int, col: Int) = {
      def xRow(x: Int) = xOffset + x * colHeight
      def yCol(y: Int) = yOffset + y * rowWidth

      new Rectangle(xRow(row), yCol(col), xRow(row + 1), yCol(col + 1))
    }

    implicit class RectangleWithBetterToString(rect: Rectangle) {
      def betterToString() = {
        "(" + rect.getLeft + ", " + rect.getBottom + "), (" + rect.getRight + ", " + rect.getTop + ")"
      }
    }

  }

}