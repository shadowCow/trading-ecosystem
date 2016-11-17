package com.cowsunday.trading.ml.transform.sliding

import com.cowsunday.trading.ml.data._

class SlidingTransforms {
  /**
   * Any sliding transforms that require multiple points of data from the PriceBars to compute.
   */
  type SlidingPriceBarTransform = Array[PriceBar] => Double

  val hiLowRange: SlidingPriceBarTransform = window => window.maxBy(_.high).high - window.minBy(_.low).low
  val openCloseRange: SlidingPriceBarTransform = window => window.maxBy(_.close).close - window.minBy(_.open).open

  val allPriceBar = List(
      hiLowRange,
      openCloseRange
      )

  type SlidingDoubleTransform = Array[Double] => Double

  val absoluteDifference: SlidingDoubleTransform = window => math.abs(window.last - window.head)
  val difference: SlidingDoubleTransform = window => window.last - window.head
  val max: SlidingDoubleTransform = window => window.max
  val min: SlidingDoubleTransform = window => window.min
  val range: SlidingDoubleTransform = window => window.max - window.min
  val average: SlidingDoubleTransform = window => window.sum / window.length

  val allDouble = List(
      absoluteDifference,
      difference,
      max,
      min,
      range,
      average
      )

  /**
   * Binary sliding transforms
   */
  type SlidingBinaryTransform = Array[Double] => BinaryVariable

  val isUpStreak: SlidingBinaryTransform = window => {
    val lagged = window.drop(1)
    val original = window.dropRight(1)
    BinaryVariable(original.zip(lagged).forall({ case (o,l) => o < l}))
  }
  val isDownStreak: SlidingBinaryTransform = window => {
    val lagged = window.drop(1)
    val original = window.dropRight(1)
    BinaryVariable(original.zip(lagged).forall({ case (o,l) => o > l}))
  }

  /**
   * Comparison of last value to window
   */
  type SlidingComparisonTransform = (Array[Double], Double) => BinaryVariable

  val isMax: SlidingComparisonTransform = (window, last) => BinaryVariable(window.max == last)
  val isMin: SlidingComparisonTransform = (window, last) => BinaryVariable(window.min == last)
}