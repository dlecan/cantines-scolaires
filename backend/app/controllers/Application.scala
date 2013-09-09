package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def menus(from: Option[String] = None, to: Option[String] = None) = TODO
  
}