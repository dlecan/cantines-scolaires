import org.joda.time.{DateTime, LocalDate}
import org.specs2.mutable._

import org.specs2.specification.Scope
import parsers._

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

      val expectedMenu1 = "Lundi 2 \nCentres de loisirs \n \n \nPique-nique "

      val expectedMenu2 = "Mardi 3 \nSalade verte / demi-œuf \nPaëlla au poulet \net fruits de mer \nFromage blanc / \nAbricots secs° "

      val expectedMenu3 = "Mardi 17 \nTaboulé \nSauté \nde bœuf* bourguignon \nCarottes \nCrème de gruyère \nPrunes "

      val (titre, menus) = pdfParser.parsePdf(getClass.getResource("/S36-37-38-39.pdf"))
      titre === "Menus du 2 au 27 septembre"
      menus must have size 20

      menus(0) === expectedMenu1
      menus(1) === expectedMenu2
      menus(11) === expectedMenu3
    }

    "Be able to parse the PDF S5-6-7-8.pdf" in {

      val expectedMenu1 = "Lundi 27 \nTarte au fromage \nSaucisse de Toulouse \nHaricots verts \nKiwi bio☺ "

      val expectedMenu2 = "Lundi 3 \nEndives au thon \nPetits panés de volaille \nChoux fleurs au gratin \nTomme noire \nTarte grillée aux fruits "

      val expectedMenu3 = "Vendredi 7 \nDés de betteraves \nDos de lieu \nau beurre blanc \nPommes \n de terre sautées \nFromage Champanet \nAnanas "

      val (titre, menus) = pdfParser.parsePdf(getClass.getResource("/S5-6-7-8.pdf"))
      titre === "Menus du 27 janvier au 21 février"
      menus must have size 20

      menus(0) === expectedMenu1
      menus(5) === expectedMenu2
      menus(9) === expectedMenu3

    }

  }
}

class FichierMenusParserSpec
  extends Specification {

  trait TestComponent
    extends MenusForPeriodParserComponentImpl
//    with Mockito
//    with PdfParserComponent
    with Scope {
//    override val pdfParser: PdfParser = mock[PdfParser]
  }

  def buildLocalDate(monthInYear: Int, dayInMonth: Int): LocalDate = new LocalDate(DateTime.now().getYear, monthInYear, dayInMonth)

  "Menus parsers" should {

    "Be able to parse a title with two dates and only one month" in new TestComponent {

      val url = getClass.getResource("/S36-37-38-39.pdf")

      val result = menusParser.parse("Menus du 2 au 27 septembre", List())

      result.du === buildLocalDate(9, 2)
      result.au === buildLocalDate(9, 27)
    }

    "Be able to parse a title with two dates and two months" in new TestComponent {

      val url = getClass.getResource("/S36-37-38-39.pdf")

      val result = menusParser.parse("Menus du 27 janvier au 21 février", List())

      result.du === buildLocalDate(1, 27)
      result.au === buildLocalDate(2, 21)
    }

  }
}
