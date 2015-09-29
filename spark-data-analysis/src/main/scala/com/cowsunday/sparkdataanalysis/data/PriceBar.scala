package com.cowsunday.sparkdataanalysis.data

class PriceBar(open: Double, high: Double, low: Double, close: Double, date: Integer) {

  def getHighLowRange = {
    high - low
  }
  
  def getOpenCloseRange = {
    math.abs(open - close)
  }
  
  def getOpen = {
    open
  }
  
  def getHigh = {
    high
  }
  
  def getLow = {
    low
  }
  
  def getClose = {
    close
  }
  
  def getDate = {
    date
  }
}