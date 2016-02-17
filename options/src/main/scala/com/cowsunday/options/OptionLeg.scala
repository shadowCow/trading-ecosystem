package com.cowsunday.options

/**
 * Represents a position in a single option.
 */
class OptionLeg(val option: Option, val entryPrice: Double, val quantity: Int, val direction: PositionDirection.Direction, val sharesPerOption: Int) {

  def this(option: Option, entryPrice: Double, quantity: Int, direction: PositionDirection.Direction) {
    this(option, entryPrice, quantity, direction, 100)
    // we assume it is a stock option by default
  }
  /**
   * Get the value of this option leg given the current price of the option
   */
  def getValue(currentPrice: Double): Double = {
    ((currentPrice - entryPrice) * direction.positionMultiplier) * quantity * sharesPerOption
  }
  
  /**
   * Get the value of this option leg at expiration given the price of the underlying
   */
  def getValueAtExpiration(underlyingPrice: Double): Double = {
    val optionPrice = math.max(0, (underlyingPrice - option.strikePrice) * OptionType.getMultiplier(option.optionType))
    getValue(optionPrice)
  }
  
}