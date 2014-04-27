package controllers

import appconfig.ProductionContext
import play.api.mvc._

object Application extends Controller with ProductionContext  {

  def menusCetteSemaine() = TODO

  def menus(from: Option[String] = None, to: Option[String] = None) = TODO
  
}