package com.cowsunday.trading.ml.data

trait PriceData {
  def getPriceBar(index: Int): PriceBar
  def getPriceBarByDate(date: Int): PriceBar
  def getPrice(priceType: PriceType.Value, index: Int): Double
  def getPriceByDate(priceType: PriceType.Value, date: Int): Double
  def getPrices(priceType: PriceType.Value, startIndex: Int, endIndex: Int): Array[Double]
  def getPricesByDates(priceType: PriceType.Value, startDate: Int, endDate: Int): Array[Double]
}