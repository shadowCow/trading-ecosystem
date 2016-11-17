package com.cowsunday.trading.ml.data

class InMemoryPriceData(opens: Array[Double], highs: Array[Double], lows: Array[Double], closes: Array[Double], dates: Array[Int]) extends PriceData {

  def getPriceBar(index: Int): PriceBar = {
    new PriceBar(opens(index), highs(index), lows(index), closes(index), dates(index))
  }

  def getPriceBarByDate(date: Int): PriceBar = {
    val indexForDate = getIndexForDate(date)

    getPriceBar(indexForDate)
  }

  def getIndexForDate(date: Int): Int = {
    var midIndex = opens.length / 2
    var minIndex = 0
    var maxIndex = opens.length - 1

    while (dates(midIndex) != date) {
      if (dates(midIndex) < date) {
        val newIndex = getMidpoint(midIndex, maxIndex)
        midIndex = if (newIndex == midIndex) newIndex + 1 else newIndex
      } else if (dates(midIndex) > date) {
        val newIndex = getMidpoint(minIndex, midIndex)
        midIndex = if (newIndex == midIndex) newIndex - 1 else newIndex
      }

      if (midIndex < 0 || midIndex >= dates.length) {
        return -1
      }
    }

    midIndex
  }

  def getMidpoint(min: Int, max: Int): Int = {
    if (max - min < 2) {
      return min
    } else {
      return min + ((max - min) / 2)
    }
  }

  def getPrice(priceType: PriceType.Value, index: Int): Double = {
    priceType match {
      case PriceType.Open  => opens(index)
      case PriceType.High  => highs(index)
      case PriceType.Low   => lows(index)
      case PriceType.Close => closes(index)
    }
  }

  def getPriceByDate(priceType: PriceType.Value, date: Int): Double = {
    val index = getIndexForDate(date)

    getPrice(priceType, index)
  }

  /**
   * Returns prices from startIndex (inclusive) to endIndex (exclusive)
   */
  def getPrices(priceType: PriceType.Value, startIndex: Int, endIndex: Int): Array[Double] = {
    priceType match {
      case PriceType.Open  => opens.slice(startIndex, endIndex)
      case PriceType.High  => highs.slice(startIndex, endIndex)
      case PriceType.Low   => lows.slice(startIndex, endIndex)
      case PriceType.Close => closes.slice(startIndex, endIndex)
    }
  }

  def getPricesByDates(priceType: PriceType.Value, startDate: Int, endDate: Int): Array[Double] = {
    val startIndex = getIndexForDate(startDate)
    val endIndex = getIndexForDate(endDate)

    getPrices(priceType, startIndex, endIndex)
  }

}