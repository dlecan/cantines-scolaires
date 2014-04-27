package services

import helpers.LoggingComponent
import repositories.MenuRepositoryComponent
import pdf.{PdfParserComponent, FichierMenusParserComponent}
import play.api.Logger
import java.net.URL
import scala.util.{Failure, Success, Try}

trait ServicesComponent {

  val menusService: MenusService
}

trait MenusService {

  def extractAndSaveMenus(url: String)

}

trait ServicesComponentImpl extends ServicesComponent {
  self: LoggingComponent
    with MenuRepositoryComponent
    with FichierMenusParserComponent
    with PdfParserComponent =>

  override val menusService: MenusService = new MenusServiceImpl

  class MenusServiceImpl extends MenusService {
    override def extractAndSaveMenus(url: String): Unit = {

      log.info(s"Will parse $url")

      val tryResult = Try {

        log.debug("Parsing pdf...")
        val (titre, strMenus) = pdfParser.parsePdf(new URL(url))
        log.debug(s"Pdf content: $titre\n$strMenus")

        log.debug("Parsing menus...")
        val menus = menusParser.parse(titre, strMenus)

        menuRepository.saveAll(menus)
        log.debug("Menus saved")
      }

      tryResult match {
        case Success(_) => log.info("Menus extracted and saved")
        case Failure(t) => log.error(s"Error while loading current PDF: $t.getMessage", t)
      }
    }
  }

}

