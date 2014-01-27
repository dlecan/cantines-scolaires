package parsers

import java.net.URL
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.{PdfTextExtractor, LocationTextExtractionStrategy, FilteredTextRenderListener, RegionTextRenderFilter}
import com.itextpdf.text.Rectangle
import org.joda.time.{Weeks, LocalDate, DateTime}
import org.joda.time.format.DateTimeFormatterBuilder

case class FichierMenus(du: LocalDate, au: LocalDate, listeMenus: List[String])

trait PdfParserComponent {
  val pdfParser: PdfParser
}

trait PdfParser {
  def parsePdf(url: URL): FichierMenus
}

trait ITextPdfParserComponent extends PdfParserComponent {

  override val pdfParser: PdfParser = new ITextPdfParser

  object ITextPdfParser {
    val xOffset = 80
    val yOffset = 16

    val rowWidth = 160
    val colHeight = 121

    // Trying to match "Menus du 2 au 27 septembre" or "Menus du 27 janvier au 21 février"
    val TitleRegexPattern = """Menus\s+du\s+(\d{1,2})\s*(\p{L}+)?\s*au\s+(\d{1,2})\s+(\p{L}+)""" r

    val dateTimeParser = new DateTimeFormatterBuilder()
      .appendYear(4, 4)
      .appendLiteral(' ')
      .appendMonthOfYearText()
      .appendLiteral(' ')
      .appendDayOfMonth(1)
      .toFormatter()
  }

  class ITextPdfParser extends PdfParser {

    import ITextPdfParser._

    override def parsePdf(url: URL): FichierMenus = {
      val reader = new PdfReader(url)

      val (du, au) = extractDates(reader)

      // Extract list of menus
      val listeMenus = for {
        row <- 0 to 3
        col <- 0 to 4
      } yield {
        extractTextFromRectangle(reader, row, col)
      }

      reader.close()
      new FichierMenus(du, au, listeMenus.toList)
    }

    private def extractDates(reader: PdfReader): (LocalDate, LocalDate) = {

      def getDate(year: Int, mois: String, jour: String): LocalDate = {
        dateTimeParser.parseDateTime(s"$year $mois $jour").toLocalDate
      }

      val year = DateTime.now().getYear()

      val title = extractTextFromRectangle(reader, new Rectangle(0, 0, 80, 800))
      title.trim match {
        case TitleRegexPattern(duStr, null, auStr, moisCommunStr) =>
          val du = getDate(year, moisCommunStr, duStr)
          val au = getDate(year, moisCommunStr, auStr)
          (du, au)
        case TitleRegexPattern(duStr, mois1Str, auStr, mois2Str) =>
          val du = getDate(year, mois1Str, duStr)
          val au = getDate(year, mois2Str, auStr)
          (du, au)
      }
    }

    private def extractTextFromRectangle(reader: PdfReader, row: Int, col: Int): String = {

      def getRectangle(row: Int, col: Int) = {
        def xRow(x: Int) = xOffset + x * colHeight
        def yCol(y: Int) = yOffset + y * rowWidth

        new Rectangle(xRow(row), yCol(col), xRow(row + 1), yCol(col + 1))
      }

      val rect = getRectangle(row, col)
      extractTextFromRectangle(reader, rect)
    }


    private def extractTextFromRectangle(reader: PdfReader, rect: Rectangle): String = {
      val filter = new RegionTextRenderFilter(rect)
      val strategy = new FilteredTextRenderListener(new LocationTextExtractionStrategy(), filter)

      PdfTextExtractor.getTextFromPage(reader, 1, strategy)
    }

    private implicit class RectangleWithBetterToString(rect: Rectangle) {
      def betterToString() = {
        "(" + rect.getLeft + ", " + rect.getBottom + "), (" + rect.getRight + ", " + rect.getTop + ")"
      }
    }

  }

}