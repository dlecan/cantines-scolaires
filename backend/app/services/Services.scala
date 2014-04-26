package services

import repositories.MenuRepositoryComponent
import parsers.{PdfParserComponent, FichierMenusParserComponent}
import play.api.{Logger, Play}
import play.api.Play.current
import java.net.URL
import scala.util.{Failure, Success, Try}

trait ServicesComponent {

  val menusService: MenusService
}

trait MenusService {

  def loadCurrentMenus()

}

trait ServicesComponentImpl extends ServicesComponent {
  self: MenuRepositoryComponent
    with FichierMenusParserComponent
    with PdfParserComponent =>

  override val menusService: MenusService = new MenusServiceImpl

  class MenusServiceImpl extends MenusService {
    override def loadCurrentMenus(): Unit = {

      val maybeUrlCurrentPdf = Play.configuration.getString("menu.pdf.url-mois-courant")

      maybeUrlCurrentPdf.foreach {
        url =>

          Logger.info(s"Will parse $url")

          val execRes = Try {

            Logger.debug("Parsing pdf...")
            val (titre, strMenus) = pdfParser.parsePdf(new URL(url))
            Logger.debug(s"Pdf content: $titre\n$strMenus")

            Logger.debug("Parsing menus...")
            val menus = menusParser.parse(titre, strMenus)

            menuRepository.saveAll(menus)
            Logger.info("Menus saved")
          }

          execRes match {
            case Success(_) =>
            case Failure(t) => Logger.error(s"Error while loading current PDF: $t.getMessage", t)
          }
      }
    }
  }

}

