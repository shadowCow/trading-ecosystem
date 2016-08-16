package com.cowsunday.sparkdataanalysis.data

object PriceType extends Enumeration with Serializable {
  type PriceType = Value
  val Open = Value("Open")
  val High = Value("High")
  val Low = Value("Low")
  val Close = Value("Close")
}