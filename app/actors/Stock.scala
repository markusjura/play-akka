package actors

import akka.actor._
import utils.StockQuote
import scala.util.Random
import scala.collection.immutable.Queue
import scala.concurrent.duration._
import play.api.libs.concurrent.Execution.Implicits._
import play.api.Play.current

object Stock {
  def props(symbol: String) = Props(new Stock(symbol))

  // Message Protocol
  trait Message
  case object FetchLatest extends Message
  case class Update(symbol: String, price: Double) extends Message
  case class History(symbol: String, history: Queue[Double]) extends Message
  case class Watch(symbol: String) extends Message
  case object Unwatch extends Message
}

/**
 * There is one Stock actor per stock symbol. The Stock actor maintains a list of users watching the stock and the stock
 * values. Each Stock actour updates a rolling dataset of randomly generated stock values.
 */
class Stock(symbol: String) extends Actor {
  import Stock._

  var watchers: Set[ActorRef] = Set.empty[ActorRef]

  // A random data set which uses stockQuote.newPrice to get each data point
  var stockHistory: Queue[Double] = {
    lazy val initialPrices: Stream[Double] = (new Random().nextDouble * 800) #:: initialPrices.map(previous => StockQuote.newPrice(previous))
    initialPrices.take(50).to[Queue]
  }

  // Fetch the latest stock value every 75ms
  val stockTick = context.system.scheduler.schedule(Duration.Zero, 75.millis, self, FetchLatest)

  def receive = {
    case FetchLatest =>
      // Add a new stock price to the history and drop the oldest
      val newPrice = updateStockHistory
      // Notify watchers
      watchers.foreach(_ ! Stock.Update(symbol, newPrice))

    case Watch(_) =>
      // Send the stock history to the user
      sender ! History(symbol, stockHistory)
      // Add the watcher to the list
      watchers = watchers + sender
  }

  // Add a new stock price to the history and drop the oldest
  // Returns the new price
  private def updateStockHistory: Double = {
    val newPrice = StockQuote.newPrice(stockHistory.last)
    stockHistory = stockHistory.drop(1) :+ newPrice
    newPrice
  }
}
