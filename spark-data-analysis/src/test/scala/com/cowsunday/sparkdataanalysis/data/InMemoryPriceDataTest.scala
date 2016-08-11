package com.cowsunday.sparkdataanalysis.data

import org.junit.Test
import org.junit.Before
import org.junit.After
import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.mutable._
import org.specs2.runner.JUnitRunner
import org.specs2.matcher.{ Expectable, Matcher }

class InMemoryPriceDataTest extends Specification {

  def createDummyPriceData(): InMemoryPriceData = {
    val opens = Array[Double](1.0, 2.0, 3.0, 4.0, 5.0)
    val highs = Array[Double](2.0, 3.0, 4.0, 5.0, 6.0)
    val lows = Array[Double](0.5, 1.5, 2.5, 3.5, 4.5)
    val closes = Array[Double](1.5, 2.5, 3.5, 4.5, 5.5)
    val dates = Array[Integer](20150101, 20150102, 20150103, 20150104, 20150105)

    val priceData = new InMemoryPriceData(opens, highs, lows, closes, dates)

    return priceData
  }

  @Test def testGetPriceBar() {
    val priceData = createDummyPriceData()

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
      case aioobe: ArrayIndexOutOfBoundsException => assert(true)
      case e: Exception => assert(false)
    }

    try {
      priceBar = priceData.getPriceBar(5)
      assert(false)
    } catch {
      case aioobe: ArrayIndexOutOfBoundsException => assert(true)
      case e: Exception => assert(false)
    }

  }

  @Test def testGetIndexForDate() {
    val priceData = createDummyPriceData()

    val index1 = priceData.getIndexForDate(20150101)
    assert(index1 == 0)

    val index2 = priceData.getIndexForDate(20150102)
    assert(index2 == 1)

    val index3 = priceData.getIndexForDate(20150103)
    assert(index3 == 2)

    val index4 = priceData.getIndexForDate(20150104)
    assert(index4 == 3)

    val index5 = priceData.getIndexForDate(20150105)
    assert(index5 == 4)

    val notFound = priceData.getIndexForDate(20200101)
    assert(notFound == -1)
  }

  @Test def testGetPrice() {
    val priceData = createDummyPriceData()

    val open1 = priceData.getPrice(PriceType.Open, 0)
    assert(open1 == 1.0)

    val high1 = priceData.getPrice(PriceType.High, 0)
    assert(high1 == 2.0)

    val low1 = priceData.getPrice(PriceType.Low, 0)
    assert(low1 == 0.5)

    val close1 = priceData.getPrice(PriceType.Close, 0)
    assert(close1 == 1.5)
  }

  @Test def testGetPriceByDate() {
    val priceData = createDummyPriceData()

    val open1 = priceData.getPriceByDate(PriceType.Open, 20150101)
    assert(open1 == 1.0)

    val open5 = priceData.getPriceByDate(PriceType.Open, 20150105)
    assert(open5 == 5.0)
  }

  @Test def testGetPrices() {
    val priceData = createDummyPriceData()

    val opens = priceData.getPrices(PriceType.Open, 0, 5)

    assert(opens === Array(1.0,2.0,3.0,4.0,5.0))

    val highs = priceData.getPrices(PriceType.High, 2, 4)
    assert(highs === Array(4.0, 5.0))
  }
}