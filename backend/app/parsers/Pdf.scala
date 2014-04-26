package parsers

import java.net.URL
import com.itextpdf.text.pdf.PdfReader
import com.itextpdf.text.pdf.parser.{PdfTextExtractor, LocationTextExtractionStrategy, FilteredTextRenderListener, RegionTextRenderFilter}
import com.itextpdf.text.Rectangle
import org.joda.time.{LocalDate, DateTime}
import org.joda.time.format.DateTimeFormatterBuilder
import scala.annotation.tailrec
import models.Menu

trait FichierMenusParserComponent {

  val menusParser: MenusForPeriodParser

}

trait MenusForPeriodParser {
  def parse(titre: String, menus: List[String]): List[Menu]
}

trait MenusForPeriodParserComponentImpl extends FichierMenusParserComponent {
//  self: PdfParserComponent =>

  override val menusParser: MenusForPeriodParser = new MenusForPeriodParserImpl()

  object FichierMenusParserImpl {
    // Trying to match "Menus du 2 au 27 septembre" or "Menus du 27 janvier au 21 février"
    val TitleRegexPattern = """Menus\s+du\s+(\d{1,2})\s*(\p{L}+)?\s*au\s+(\d{1,2})\s+(\p{L}+)""" r

    // Trying to match "Lundi 2" or "Vendredi 27"
    val DayTitleRegexPattern = """\p{L}+\s+(\d{1,2})\s*""" r

    val dateTimeParser = new DateTimeFormatterBuilder()
      .appendYear(4, 4)
      .appendLiteral(' ')
      .appendMonthOfYearText()
      .appendLiteral(' ')
      .appendDayOfMonth(1)
      .toFormatter()
  }

  class MenusForPeriodParserImpl extends MenusForPeriodParser {

    import FichierMenusParserImpl._

    override def parse(titre: String, menus: List[String]): List[Menu] = {

      val (du, au) = parseTitleDate(titre)

      val menuDates = allMenuDates(du, au)
                      .map(d => (d.getDayOfMonth, d))
                      .toMap

      menus.map(parseIndividualTitleDate(_, menuDates))
    }

    private def parseTitleDate(title: String): (LocalDate, LocalDate) = {

      def getDate(year: Int, mois: String, jour: String): LocalDate = {
        dateTimeParser.parseDateTime(s"$year $mois $jour").toLocalDate
      }

      val year = DateTime.now().getYear()

      title match {
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

    private def parseIndividualTitleDate(strMenu: String, menuDates: Map[Int, LocalDate]): Menu = {
      val firstLF = strMenu.indexOf('\n')

      val title = strMenu.substring(0, firstLF)

      val DayTitleRegexPattern(numJour) = title

      val dateMenu = menuDates(numJour.toInt)

      val textMenu = strMenu.substring(firstLF + 1, strMenu.size)

      Menu(dateMenu, textMenu)
    }

    private def allMenuDates(du: LocalDate, au: LocalDate): List[LocalDate] = {
      @tailrec
      def loop(acc: List[LocalDate], current: LocalDate): List[LocalDate] = {
        if (current.isAfter(au)) {
          acc
        } else {
          val newAcc = acc :+ current
          loop(newAcc, current.plusDays(1))
        }
      }
      loop(List(), du)
    }

  }

}


trait PdfParserComponent {
  val pdfParser: PdfParser
}

trait PdfParser {
  def parsePdf(url: URL): (String, List[String])
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

    override def parsePdf(url: URL): (String, List[String]) = {
      val reader = new PdfReader(url)

      val title = extractTextFromRectangle(reader, new Rectangle(0, 0, 80, 800)).trim

      // Extract list of menus
      val listeMenus = for {
        row <- 0 to 3
        col <- 0 to 4
      } yield {
        extractTextFromRectangle(reader, row, col)
      }

      reader.close()
      (title, listeMenus.toList)
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