package com.cowsunday.sparkdataanalysis.data

import org.scalatest.Assertions
import org.junit.Test

class PriceTypeTest extends Assertions {

  @Test def priceTypesShouldHaveCorrectStringValues() {
    assert(PriceType.Open.toString === "Open")
    assert(PriceType.High.toString === "High")
    assert(PriceType.Low.toString === "Low")
    assert(PriceType.Close.toString === "Close")
  }
}