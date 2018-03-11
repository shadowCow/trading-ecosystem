package com.cowsunday.trading.ml.data

case class PriceDataSchema(date: String,
                           time: String,
                           open: String,
                           high: String,
                           low: String,
                           close: String,
                           atr: String,
                           volume: String)

object PriceDataSchemas {

  val v1 = PriceDataSchema(
    "trading_date",
    "trading_time",
    "o",
    "h",
    "l",
    "c",
    "atr",
    "volume"
  )

}
