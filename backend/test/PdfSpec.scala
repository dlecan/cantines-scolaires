import org.joda.time.{DateTime, LocalDate}
import org.specs2.mutable._
import org.specs2.mock._

import org.specs2.specification.Scope
import parsers._
import parsers.DonneesBrutes

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class PdfParserSpec
  extends Specification
  with ITextPdfParserComponent {

  "PDF parsers" should {

    "Be able to parse the PDF S36-37-38-39.pdf" in {

      val result = pdfParser.parsePdf(getClass.getResource("/S36-37-38-39.pdf"))
      result.titre === "Menus du 2 au 27 septembre"
      result.menus must have size 20
    }

    "Be able to parse the PDF S5-6-7-8.pdf" in {

      val result = pdfParser.parsePdf(getClass.getResource("/S5-6-7-8.pdf"))
      result.titre === "Menus du 27 janvier au 21 février"
      result.menus must have size 20
    }

  }
}

class FichierMenusParserSpec
  extends Specification {

  trait TestComponent
    extends Mockito
    with FichierMenusParserComponentImpl
    with PdfParserComponent
    with Scope {
    override val pdfParser: PdfParser = mock[PdfParser]
  }

  def buildLocalDate(monthInYear: Int, dayInMonth: Int): LocalDate = new LocalDate(DateTime.now().getYear, monthInYear, dayInMonth)

  "Menus parsers" should {

    "Be able to parse a title with two dates and only one month" in new TestComponent {

      val url = getClass.getResource("/S36-37-38-39.pdf")
      val expected = new DonneesBrutes("Menus du 2 au 27 septembre", List())

      pdfParser.parsePdf(url) returns expected

      val result = menusParser.parse(url)

      result.du === buildLocalDate(9, 2)
      result.au === buildLocalDate(9, 27)
    }

    "Be able to parse a title with two dates and two months" in new TestComponent {

      val url = getClass.getResource("/S36-37-38-39.pdf")
      val expected = new DonneesBrutes("Menus du 27 janvier au 21 février", List())

      pdfParser.parsePdf(url) returns expected

      val result = menusParser.parse(url)

      result.du === buildLocalDate(1, 27)
      result.au === buildLocalDate(2, 21)
    }

  }
}
