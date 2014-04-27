package services

import helpers.{LoggingComponentImpl, LoggingComponent}
import org.specs2.mock.Mockito
import org.specs2.mutable._

import org.specs2.specification.Scope
import pdf.{MenusForPeriodParser, PdfParser, PdfParserComponent, FichierMenusParserComponent}
import repositories.{MenuRepositoryComponent, MenuRepository}
import models.Menu
import play.api.LoggerLike
import org.specs2.matcher.ThrownExpectations
import java.net.URL
import org.hamcrest.core.IsAnything

trait DataSet {

}

class MenusServiceSpec
  extends Specification
  with DataSet
  with Mockito {

  trait TestComponent
    extends ServicesComponentImpl
    with LoggingComponentImpl
    with MenuRepositoryComponent
    with FichierMenusParserComponent
    with PdfParserComponent
    with Scope {

    val spiedLog = spy(super.log)

    override def log = spiedLog
    override val menuRepository = mock[MenuRepository]
    override val menusParser = mock[MenusForPeriodParser]
    override val pdfParser = mock[PdfParser]
  }

  "Menus service" should {

    "Load current pdf of menus" in new TestComponent {

      val url = "http://url_de_test"
      val expectedRes = ("", List[String]())

      // When
      pdfParser.parsePdf(any[URL]) returns expectedRes
      menusParser.parse("", List()) returns List()

      menusService.extractAndSaveMenus(url)

      // Then
//      def anyFunction0[String] = anArgThat(new IsAnything[Function0[String]])
//      val truc = () => anyString
//      there was one(log).info(anyFunction0[String]())
    }
  }
}
