package models

import org.joda.time.{LocalDate, DateTime}
import play.api.libs.json.Json

case class Menu(date: LocalDate, menu: String)

object Menu {
  implicit val fmt = Json.format[Menu]
}
