# Play Akka
This is a showcase how to use Akka together with Play. This app was used for a coding workshop.

The corresponding slides can be found [here](markusjura.github.io/play-akka-slides)

## Helpers

### index.coffee
```
$ ->
  ws = new WebSocket $("body").data("ws-url")
  ws.onmessage = (event) ->
    message = JSON.parse event.data
    switch message.type
      when "stockhistory"
        populateStockHistory(message)
      when "stockupdate"
        updateStockChart(message)
      else
        console.log(message)
```

```
  $("#addsymbolform").submit (event) ->
    event.preventDefault()
    # send the message to watch the stock
    ws.send(JSON.stringify({symbol: $("#addsymboltext").val()}))
    # reset the form
    $("#addsymboltext").val("")
```

### Stock
```
var stockHistory: Queue[Double] = {
  lazy val initialPrices: Stream[Double] = (new Random().nextDouble * 800) #:: initialPrices.map(previous => StockQuote.newPrice(previous))
  initialPrices.take(50).to[Queue]
}
```

### User
```
val message = Json.obj(
  "type" -> "stockupdate",
  "symbol" -> symbol,
  "price" -> price)
```
 
```
val message = Json.obj(
  "type" -> "stockhistory",
  "symbol" -> symbol,
  "history" -> history)
```