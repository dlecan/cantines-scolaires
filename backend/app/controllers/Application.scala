package controllers

import appconfig.ProdEnvironment
import play.api._
import play.api.mvc._

object Application extends Controller with ProdEnvironment  {

  def menusCetteSemaine() = TODO

  def menus(from: Option[String] = None, to: Option[String] = None) = TODO
  
}