package actors

import akka.actor._
import play.api.Logger
import play.api.libs.json._
import scala.collection.JavaConversions._
import play.api.Play.current

object User {
  def props(out: ActorRef) = Props(new User(out))
}

/**
 * User actor which receives and sends events to the client
 * @param out websocket actor
 */
class User(out: ActorRef) extends Actor {

  val defaultStocks = play.api.Play.configuration.getStringList("default.stocks").get
  // New user watches default stocks
  defaultStocks.foreach(symbol => StocksDelegator.ref ! Stock.Watch(symbol))

  def receive = {
    case msg: JsValue =>
      // Parse the symbol from JSON
      val symbol = (msg \ "symbol").as[String]
      // Send the Watch message to the stocks actor
      StocksDelegator.ref.tell(Stock.Watch(symbol), self)

    case Stock.Update(symbol, price) =>
      val message = Json.obj(
        "type" -> "stockupdate",
        "symbol" -> symbol,
        "price" -> price)
      out ! message

    case Stock.History(symbol, history) =>
      val message = Json.obj(
        "type" -> "stockhistory",
        "symbol" -> symbol,
        "history" -> history)
      out ! message
  }
}
