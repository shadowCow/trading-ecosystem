package com.cowsunday.trading.ml.data

@SerialVersionUID(1986L)
case class PriceBar(open: Double, high: Double, low: Double, close: Double, date: Long) extends Serializable {

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

  /**
   * Returns the absolute value of (close - open)
   */
  def getOpenCloseRange = {
    math.abs(open - close)
  }

  /**
   * Returns close - open
   */
  def getOpenCloseChange = {
    close - open
  }

  def isUp = {
    close > open
  }

  def isDown = {
    close < open
  }

  def isSideways = {
    close == open
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

  override def equals(obj: Any) = {

    if (obj.isInstanceOf[PriceBar]) {
      val other = obj.asInstanceOf[PriceBar]

      other.getDate == this.getDate && other.getOpen == this.getOpen && other.getHigh == this.getHigh && other.getLow == this.getLow && other.getClose == this.getClose
    } else {
      false
    }
  }
}