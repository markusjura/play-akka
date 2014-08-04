package actors

import akka.actor.{Props, ActorRef, Actor}
import play.api.libs.concurrent.Akka
import play.api.Play.current

object StocksDelegator {
  val ref: ActorRef = Akka.system.actorOf(Props(new StocksDelegator))
}

class StocksDelegator extends Actor {

  def receive = {
    case watchStock @ Stock.Watch(symbol) =>
      // get or create the StockActor for the symbol and forward this message
      context.child(symbol).getOrElse {
        context.actorOf(Stock.props(symbol), symbol)
      } forward watchStock

    case unwatchStock @ Stock.Unwatch =>
      // Forward the message to everyone
      context.children.foreach(_.forward(unwatchStock))
  }
}
