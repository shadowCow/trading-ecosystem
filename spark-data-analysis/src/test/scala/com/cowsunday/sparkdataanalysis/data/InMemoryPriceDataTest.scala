package com.cowsunday.sparkdataanalysis.data

import org.scalatest.Assertions
import org.junit.Test

class InMemoryPriceDataTest extends Assertions {

  @Test def priceDataHasCorrectValues() {
    val opens = Array[Double](1.0, 2.0, 3.0, 4.0, 5.0)
    val highs = Array[Double](2.0, 3.0, 4.0, 5.0, 6.0)
    val lows = Array[Double](0.5, 1.5, 2.5, 3.5, 4.5)
    val closes = Array[Double](1.5, 2.5, 3.5, 4.5, 5.5)
    val dates = Array[Integer](20150101, 20150102, 20150103, 20150104, 20150105)
    
    val priceData = new InMemoryPriceData(opens, highs, lows, closes, dates)
    
    // priceBar access
    var priceBar = priceData.getPriceBar(0)
    assert(priceBar.getOpen === 1.0)
    assert(priceBar.getHigh === 2.0)
    assert(priceBar.getLow === 0.5)
    assert(priceBar.getClose === 1.5)
    assert(priceBar.getDate === 20150101)
    
    priceBar = priceData.getPriceBar(4)
    assert(priceBar.getOpen === 5.0)
    assert(priceBar.getHigh === 6.0)
    assert(priceBar.getLow === 4.5)
    assert(priceBar.getClose === 5.5)
    assert(priceBar.getDate === 20150105)
    
    try {
      priceBar = priceData.getPriceBar(-1)
      assert(false)
    } catch {
      case npe: ArrayIndexOutOfBoundsException => assert(true)
      case e: Exception => assert(false)
    }    
    
    try {
      priceBar = priceData.getPriceBar(5)
      assert(false)
    } catch {
      case npe: ArrayIndexOutOfBoundsException => assert(true)
      case e: Exception => assert(false)
    }
    
    
  }
}