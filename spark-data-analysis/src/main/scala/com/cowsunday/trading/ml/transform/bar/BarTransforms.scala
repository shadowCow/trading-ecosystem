package com.cowsunday.trading.ml.transform.bar

import com.cowsunday.trading.ml.data._

class BarTransforms {
  type BarTransform = PriceBar => Double

  val close: BarTransform = bar => bar.close
  val closeHighRange: BarTransform = bar => bar.high - bar.close
  val closeLowRange: BarTransform = bar => bar.close - bar.low
  val high: BarTransform = bar => bar.high
  val hiLowRange: BarTransform = bar => bar.high - bar.low
  val low: BarTransform = bar => bar.low
  val open: BarTransform = bar => bar.open
  val openCloseChange: BarTransform = bar => bar.getOpenCloseChange
  val openCloseRange: BarTransform = bar => bar.getOpenCloseRange
  val openHighRange: BarTransform = bar => bar.high - bar.open
  val openLowRange: BarTransform = bar => bar.open - bar.low

  val all: List[BarTransform] = List(
      close,
      closeHighRange,
      closeLowRange,
      high,
      hiLowRange,
      low,
      open,
      openCloseChange,
      openCloseRange,
      openHighRange,
      openLowRange
      )
}