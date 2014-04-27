package helpers

import play.api.{Logger, LoggerLike}

trait LoggingComponent {

  def log: LoggerLike

}

trait LoggingComponentImpl extends LoggingComponent {
  override def log: LoggerLike = Logger("cantine")
}