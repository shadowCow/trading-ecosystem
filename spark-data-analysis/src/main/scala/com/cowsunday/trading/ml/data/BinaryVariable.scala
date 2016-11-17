package com.cowsunday.trading.ml.data

case class BinaryVariable(val truthy: Boolean) {
  val value = if (truthy) { 1 } else { 0 }
}
