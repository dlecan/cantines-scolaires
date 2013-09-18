package models

import org.joda.time.DateTime
import play.api.libs.json.Json

case class Menu(date: DateTime, menu: String)

object Menu {
  implicit val fmt = Json.format[Menu]
}
