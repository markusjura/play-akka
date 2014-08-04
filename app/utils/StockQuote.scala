package utils

import scala.util.Random

object StockQuote {
  def newPrice(lastPrice: Double): Double =
    lastPrice * (0.95 + (0.1 * new Random().nextDouble))
}
