package com.cowsunday.sparkdataanalysis.data

class InMemoryPriceData(opens: Array[Double], highs: Array[Double], lows: Array[Double], closes: Array[Double], dates: Array[Integer]) extends PriceData {

  def getPriceBar(index: Integer): PriceBar = {
    new PriceBar(opens(index), highs(index), lows(index), closes(index), dates(index))
  }

  def getPriceBarByDate(date: Integer): PriceBar = {
    val indexForDate = getIndexForDate(date)

    getPriceBar(indexForDate)
  }

  def getIndexForDate(date: Integer): Integer = {
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
  
  def getMidpoint(min: Integer, max: Integer): Integer = {
    if (max - min < 2) {
      return min
    } else {
      return min + ((max - min) / 2)
    }
  }

  def getPrice(priceType: PriceType.Value, index: Integer): Double = {
    priceType match {
      case PriceType.Open  => opens(index)
      case PriceType.High  => highs(index)
      case PriceType.Low   => lows(index)
      case PriceType.Close => closes(index)
    }
  }

  def getPriceByDate(priceType: PriceType.Value, date: Integer): Double = {
    val index = getIndexForDate(date)

    getPrice(priceType, index)
  }

  /**
   * Returns prices from startIndex (inclusive) to endIndex (exclusive)
   */
  def getPrices(priceType: PriceType.Value, startIndex: Integer, endIndex: Integer): Array[Double] = {
    priceType match {
      case PriceType.Open  => opens.slice(startIndex, endIndex)
      case PriceType.High  => highs.slice(startIndex, endIndex)
      case PriceType.Low   => lows.slice(startIndex, endIndex)
      case PriceType.Close => closes.slice(startIndex, endIndex)
    }
  }

  def getPricesByDates(priceType: PriceType.Value, startDate: Integer, endDate: Integer): Array[Double] = {
    val startIndex = getIndexForDate(startDate)
    val endIndex = getIndexForDate(endDate)

    getPrices(priceType, startIndex, endIndex)
  }

}