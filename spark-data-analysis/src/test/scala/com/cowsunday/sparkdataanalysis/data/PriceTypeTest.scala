package com.cowsunday.sparkdataanalysis.data

import org.junit.Test
import org.junit.Before
import org.junit.After
import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.mutable._
import org.specs2.runner.JUnitRunner
import org.specs2.matcher.{ Expectable, Matcher }

class PriceTypeTest extends Specification {

  @Test def priceTypesShouldHaveCorrectStringValues() {
    assert(PriceType.Open.toString === "Open")
    assert(PriceType.High.toString === "High")
    assert(PriceType.Low.toString === "Low")
    assert(PriceType.Close.toString === "Close")
  }
}