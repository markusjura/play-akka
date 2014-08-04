package actors

import akka.actor.{Props, Actor}
import play.api.Logger
import utils.StockQuote
import scala.util.Random
import scala.collection.immutable.Queue

object Stock {
  def props(symbol: String) = Props(new Stock(symbol))

  // Message Protocol
  trait Message
  case class Watch(symbol: String) extends Message
  case object Unwatch extends Message
}

/**
 * There is one Stock actor per stock symbol. The Stock actor maintains a list of users watching the stock and the stock
 * values. Each Stock actour updates a rolling dataset of randomly generated stock values.
 */
class Stock(symbol: String) extends Actor {

  // A random data set which uses stockQuote.newPrice to get each data point
  var stockHistory: Queue[Double] = {
    lazy val initialPrices: Stream[Double] = (new Random().nextDouble * 800) #:: initialPrices.map(previous => StockQuote.newPrice(previous))
    initialPrices.take(50).to[Queue]
  }

  def receive = {
    case message => Logger.debug(s"Stocks actor received message $message")
  }

  // Add a new stock price to the history and drop the oldest
  // Returns the new price
  private def updateStockHistory: Double = {
    val newPrice = StockQuote.newPrice(stockHistory.last)
    stockHistory = stockHistory.drop(1) :+ newPrice
    newPrice
  }
}
