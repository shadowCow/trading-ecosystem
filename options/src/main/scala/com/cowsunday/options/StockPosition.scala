package com.cowsunday.options

/**
 * Represents a position in a single stock.
 */
class StockPosition(val symbol: String, val entryPrice: Double, val quantity: Int, val positionDirection: PositionDirection.Direction) {

  def getNetGain(currentPrice: Double): Double = {
    ((currentPrice - entryPrice) * positionDirection.positionMultiplier) * quantity
  }
  
}