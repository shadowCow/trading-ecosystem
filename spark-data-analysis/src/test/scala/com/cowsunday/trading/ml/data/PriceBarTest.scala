package com.cowsunday.trading.ml.data

import org.junit.Test
import org.junit.Before
import org.junit.After
import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.mutable._
import org.specs2.runner.JUnitRunner

import com.cowsunday.trading.ml.data.PriceBar;
import org.specs2.matcher.{ Expectable, Matcher }

class PriceBarTest extends Specification {

  @Test def priceBarHasCorrectValues() {
    val priceBar = new PriceBar(2.0, 4.0, 1.0, 3.0, 20150101)
    assert(priceBar.getOpen === 2.0)
    assert(priceBar.getHigh === 4.0)
    assert(priceBar.getLow === 1.0)
    assert(priceBar.getClose === 3.0)
    assert(priceBar.getHighLowRange === 3.0)
    assert(priceBar.getOpenCloseRange === 1.0)
    assert(priceBar.getDate === 20150101)

  }

  @Test def testToStringWorks() {
    val priceBar = new PriceBar(2.0, 4.0, 1.0, 3.0, 20150101)
    val string = priceBar.toString

    assert(string === "{date: 20150101, open: 2.0, high: 4.0, low: 1.0, close: 3.0}")
  }
}