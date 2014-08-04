package actors

import akka.actor._
import play.api.Logger
import play.api.libs.json._

object User {
  def props(out: ActorRef) = Props(new User(out))
}

/**
 * User actor which receives and sends events to the client
 * @param out websocket actor
 */
class User(out: ActorRef) extends Actor {

  val initMsg = "WebSocket connection established"
  Logger.debug(initMsg)
  out ! Json.obj("msg" -> initMsg)

  def receive = {
    case message: JsValue =>
      Logger.debug(s"New WebSocket message: $message")
      out ! Json.obj("msg" -> message)
  }
}