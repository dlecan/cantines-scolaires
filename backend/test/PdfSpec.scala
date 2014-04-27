import models.Menu
import org.joda.time.{DateTime, LocalDate}
import org.specs2.mutable._

import org.specs2.specification.Scope
import pdf._

trait DataSet {

  val expectedStrMenu1PDF1 = "Lundi 2 \nCentres de loisirs \n \n \nPique-nique "
  val expectedStrMenu2PDF1 = "Mardi 3 \nSalade verte / demi-œuf \nPaëlla au poulet \net fruits de mer \nFromage blanc / \nAbricots secs° "
  val expectedStrMenu3PDF1 = "Mardi 17 \nTaboulé \nSauté \nde bœuf* bourguignon \nCarottes \nCrème de gruyère \nPrunes "

  val expectedStrMenu1PDF2 = "Lundi 27 \nTarte au fromage \nSaucisse de Toulouse \nHaricots verts \nKiwi bio☺ "
  val expectedStrMenu2PDF2 = "Lundi 3 \nEndives au thon \nPetits panés de volaille \nChoux fleurs au gratin \nTomme noire \nTarte grillée aux fruits "
  val expectedStrMenu3PDF2 = "Vendredi 7 \nDés de betteraves \nDos de lieu \nau beurre blanc \nPommes \n de terre sautées \nFromage Champanet \nAnanas "

  val expectedMenu1PDF1 = Menu(buildLocalDate(9, 2), "Centres de loisirs \n \n \nPique-nique ")
  val expectedMenu2PDF1 = Menu(buildLocalDate(9, 3), "Salade verte / demi-œuf \nPaëlla au poulet \net fruits de mer \nFromage blanc / \nAbricots secs° ")
  val expectedMenu3PDF1 = Menu(buildLocalDate(9, 17), "Taboulé \nSauté \nde bœuf* bourguignon \nCarottes \nCrème de gruyère \nPrunes ")

  val expectedMenu1PDF2 = Menu(buildLocalDate(1, 27), "Tarte au fromage \nSaucisse de Toulouse \nHaricots verts \nKiwi bio☺ ")
  val expectedMenu2PDF2 = Menu(buildLocalDate(2, 3), "Endives au thon \nPetits panés de volaille \nChoux fleurs au gratin \nTomme noire \nTarte grillée aux fruits ")
  val expectedMenu3PDF2 = Menu(buildLocalDate(2, 7), "Dés de betteraves \nDos de lieu \nau beurre blanc \nPommes \n de terre sautées \nFromage Champanet \nAnanas ")

  def buildLocalDate(monthInYear: Int, dayInMonth: Int): LocalDate = new LocalDate(DateTime.now().getYear, monthInYear, dayInMonth)

}

class PdfParserSpec
  extends Specification
  with ITextPdfParserComponent
  with DataSet {

  "PDF parsers" should {

    "Be able to parse the PDF S36-37-38-39.pdf" in {

      val (titre, menus) = pdfParser.parsePdf(getClass.getResource("/S36-37-38-39.pdf"))
      titre === "Menus du 2 au 27 septembre"
      menus must have size 20

      menus(0) === expectedStrMenu1PDF1
      menus(1) === expectedStrMenu2PDF1
      menus(11) === expectedStrMenu3PDF1
    }

    "Be able to parse the PDF S5-6-7-8.pdf" in {

      val (titre, menus) = pdfParser.parsePdf(getClass.getResource("/S5-6-7-8.pdf"))
      titre === "Menus du 27 janvier au 21 février"
      menus must have size 20

      menus(0) === expectedStrMenu1PDF2
      menus(5) === expectedStrMenu2PDF2
      menus(9) === expectedStrMenu3PDF2

    }

  }
}

class FichierMenusParserSpec
  extends Specification
  with DataSet {

  trait TestComponent
    extends MenusForPeriodParserComponentImpl
    //    with Mockito
    //    with PdfParserComponent
    with Scope {
    //    override val pdfParser: PdfParser = mock[PdfParser]
  }

  "Menus parsers" should {

    "Be able to parse a title with two dates and only one month" in new TestComponent {

      val menus = menusParser.parse("Menus du 2 au 27 septembre",
        List(expectedStrMenu1PDF1, expectedStrMenu2PDF1, expectedStrMenu3PDF1))

      menus(0) === expectedMenu1PDF1
      menus(1) === expectedMenu2PDF1
      menus(2) === expectedMenu3PDF1
    }

    "Be able to parse a title with two dates and two months" in new TestComponent {

      val menus = menusParser.parse("Menus du 27 janvier au 21 février",
        List(expectedStrMenu1PDF2, expectedStrMenu2PDF2, expectedStrMenu3PDF2))

      menus(0) === expectedMenu1PDF2
      menus(1) === expectedMenu2PDF2
      menus(2) === expectedMenu3PDF2
    }

  }
}
