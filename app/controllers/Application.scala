package controllers

import play.api._
import play.api.libs.json.JsValue
import play.api.mvc._
//import actors._
import play.api.Play.current

object Application extends Controller {

  // TODO: Render index page
  def index = Action { implicit request =>
    NotImplemented
  }

  // TODO: Create WebSocket connection to push new stock prices to the client
}
