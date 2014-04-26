import appconfig.ProdEnvironment
import play.api._

object Global extends GlobalSettings with ProdEnvironment {

  override def onStart(app: Application): Unit = {

  }
}