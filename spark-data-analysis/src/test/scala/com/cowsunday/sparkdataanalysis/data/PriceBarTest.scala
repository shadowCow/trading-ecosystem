package com.cowsunday.sparkdataanalysis.data

import org.scalatest.Assertions
import org.junit.Test

class PriceBarTest extends Assertions {

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