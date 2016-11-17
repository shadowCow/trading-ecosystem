package com.cowsunday.trading.ml.data

object ComparisonVariable extends Enumeration with Serializable {
  type ComparisonVariable = Value
  val lessThan = -1
  val equalTo = 0
  val greaterThan = 1
}