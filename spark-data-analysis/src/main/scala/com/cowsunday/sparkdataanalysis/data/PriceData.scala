package com.cowsunday.sparkdataanalysis.data

trait PriceData {
  def getPriceBar(index: Integer): PriceBar
  def getPriceBarByDate(date: Integer): PriceBar
  def getPrice(priceType: PriceType.Value, index: Integer): Double
  def getPriceByDate(priceType: PriceType.Value, date: Integer): Double
  def getPrices(priceType: PriceType.Value, startIndex: Integer, endIndex: Integer): Array[Double]
  def getPricesByDates(priceType: PriceType.Value, startDate: Integer, endDate: Integer): Array[Double]
}