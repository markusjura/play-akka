package controllers

import play.api._
import play.api.libs.json.JsValue
import play.api.mvc._
import actors._
import play.api.Play.current
import actors._

object Application extends Controller {

  def index = Action { implicit request =>
    Ok(views.html.index())
  }

  /**
   * Establishing a WebSocket connection with an actor
   * Input type: JsValue
   * Output type (out): JsValue
   */
  def ws = WebSocket.acceptWithActor[JsValue, JsValue] { request => out =>
    User.props(out)
  }
}
