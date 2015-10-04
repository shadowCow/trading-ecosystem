package com.cowsunday.sparkdataanalysis.data

@SerialVersionUID(1986L)
class PriceBar(open: Double, high: Double, low: Double, close: Double, date: Integer) extends Serializable {

  def getPrice(priceType: PriceType.Value): Double = {
    priceType match {
      case PriceType.Open => open
      case PriceType.High => high
      case PriceType.Low => low
      case PriceType.Close => close
    }  
  }
  
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
  
  override def toString(): String = {
    "{date: " + date + ", open: " + open + ", high: " + high + ", low: " + low + ", close: " + close + "}"
  }
}