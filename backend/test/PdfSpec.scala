import org.joda.time.{DateTime, LocalDate}
import org.specs2.mutable._

import parsers._
import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class PdfSpec extends Specification {

  class TestComponent extends ITextPdfParserComponent

  val component = new TestComponent
  val pdfParser: PdfParser = component.pdfParser

  "PDF parsers" should {

    "Be able to parse a PDF S36-37-38-39.pdf" in {

      val result = pdfParser.parsePdf(getClass.getResource("/S36-37-38-39.pdf"))

      result.du === new LocalDate(DateTime.now().getYear(), 9, 2)
      result.au === new LocalDate(DateTime.now().getYear(), 9, 27)
      result.listeMenus must have size 20
    }

    "Be able to parse a PDF S5-6-7-8.pdf" in {

      val result = pdfParser.parsePdf(getClass.getResource("/S5-6-7-8.pdf"))

      result.du === new LocalDate(DateTime.now().getYear(), 1, 27)
      result.au === new LocalDate(DateTime.now().getYear(), 2, 21)
      result.listeMenus must have size 20
    }

  }
}