import appconfig.ProductionContext
import play.api._

object Global extends GlobalSettings with ProductionContext {

  override def onStart(app: Application): Unit = {

  }
}