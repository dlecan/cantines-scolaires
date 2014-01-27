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

    "Be able to parse a PDF 2013_36-37-38-39.pdf" in {

      pdfParser.parsePdf(getClass.getResource("/2013_36-37-38-39.pdf")) must have size 20
    }

    "Be able to parse a PDF 2014_5-6-7-8.pdf" in {

      pdfParser.parsePdf(getClass.getResource("/2014_5-6-7-8.pdf")) must have size 20
    }

  }
}