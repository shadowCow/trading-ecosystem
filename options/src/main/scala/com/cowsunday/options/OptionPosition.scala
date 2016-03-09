package com.cowsunday.options

import scala.collection.mutable.ListBuffer

/**
 * Represents a position in multiple options.
 * All options have the same expiration, and the same underlying asset.
 */
class OptionPosition(val optionLegs: List[OptionLeg], val stockPosition: StockPosition) {
  
  val valueIntervals = getValueIntervals(optionLegs)
  val positiveValueIntervals = valueIntervals.filter { interval => interval.isPositiveValuesOnly() }
  val negativeValueIntervals = valueIntervals.filter { interval => interval.isNegativeValuesOnly() }
  val maxGain = computeMaxGain(positiveValueIntervals)
  val maxLoss = computeMaxLoss(negativeValueIntervals)
  
  def this(optionLegs: List[OptionLeg]) {
    this(optionLegs, null)
  }
  
  def this(optionLeg: OptionLeg) {
    this(List(optionLeg))
  }
  
  /**
   * We need to deal with off-setting options before we sum up values, otherwise we might be adding positive and negative infinity together, which is undefined.
   */
  def getNetGainAtExpiration(underlyingPrice: Double): Double = {
    if (underlyingPrice == Double.PositiveInfinity) {
      // if the underlying price is positive infinity, we have to account for off-setting calls
      val longCalls = optionLegs.filter { leg => leg.direction == PositionDirection.LONG && leg.option.optionType == OptionType.CALL }
      val shortCalls = optionLegs.filter { leg => leg.direction == PositionDirection.SHORT && leg.option.optionType == OptionType.CALL }
      
      // multiply option leg quantity by 100 so we can combine with the stock position easily
      val longCallQuantity = longCalls.map { leg => leg.quantity * 100 }.sum
      val shortCallQuantity = shortCalls.map { leg => leg.quantity *100 }.sum
      // add the stock position quantity to the call quantity
      val totalLongQuantity = longCallQuantity + {
        if (stockPosition != null && stockPosition.positionDirection == PositionDirection.LONG) stockPosition.quantity else 0
      }
      val totalShortQuantity = shortCallQuantity + {
        if (stockPosition != null && stockPosition.positionDirection == PositionDirection.SHORT) stockPosition.quantity else 0
      }
      
      if (totalLongQuantity > totalShortQuantity) {
        // at least one call or stock will be positive infinity, and all shorts will be offset, so its just positive infinity
        Double.PositiveInfinity
      } else if (totalLongQuantity < totalShortQuantity){
        // at least one call or stock will be negative infinity, and all longs will be offset, so its just negative infinity
        Double.NegativeInfinity
      } else {
        // since we know the call quantities offset, we can do a neat trick by using strike values and quantities to get their net value
        val longCallStrikeVals = longCalls.map { call => call.quantity * 100 * (call.option.strikePrice * -1 - call.entryPrice) }.sum
        val shortCallStrikeVals = shortCalls.map { call => call.quantity * 100 * (call.option.strikePrice + call.entryPrice) }.sum
        // incorporate stock with same trick
        val totalLongValue = longCallStrikeVals + {
          if (stockPosition != null && stockPosition.positionDirection == PositionDirection.LONG) stockPosition.quantity * stockPosition.entryPrice * -1 else 0
        }
        val totalShortValue = shortCallStrikeVals + {
          if (stockPosition != null && stockPosition.positionDirection == PositionDirection.SHORT) stockPosition.quantity * stockPosition.entryPrice else 0
        }
        
        val putValues = optionLegs.filter { leg => leg.option.optionType == OptionType.PUT }.map { leg => leg.getNetGainAtExpiration(underlyingPrice) }.sum

        totalLongValue + totalShortValue + putValues
      }
    } else {
      // if underlying price is not positive infinity, then all legs have finite value, and we can just do a simple sum
      val optionLegsValue = optionLegs.map { leg => leg.getNetGainAtExpiration(underlyingPrice) }.sum  
      val stockPositionValue = if (stockPosition != null) stockPosition.getNetGain(underlyingPrice) else 0 
      optionLegsValue + stockPositionValue
    }
  }
  
  private def getValueIntervals(optionLegs: List[OptionLeg]): List[LinearValueInterval] = {
    val strikes = optionLegs.map { leg => leg.option.strikePrice }.union(List(0, Double.PositiveInfinity)).distinct.sorted
    
    val intervals = ListBuffer[LinearValueInterval]()
    for (i <- 1 until strikes.length) {
      if (strikes(i) == Double.PositiveInfinity) {
        intervals ++= ValueIntervalUtils.getValueIntervalsWithInfinity(strikes(i-1), strikes(i), getNetGainAtExpiration(strikes(i-1)), getNetGainAtExpiration(strikes(i)), strikes(i-1)*2, getNetGainAtExpiration(strikes(i-1)*2))
      } else {
        intervals ++= ValueIntervalUtils.getValueIntervals(strikes(i-1), strikes(i), getNetGainAtExpiration(strikes(i-1)), getNetGainAtExpiration(strikes(i))) 
      }
    }
    intervals.toList
  }
  
  def computeMaxGain(valueIntervals: List[LinearValueInterval]): Double = {
    if (valueIntervals.isEmpty) {
      Double.NaN
    } else {
      valueIntervals.maxBy { interval => interval.maxValue() }.maxValue() 
    }
  }
  
  def computeMaxLoss(valueIntervals: List[LinearValueInterval]): Double = {
    if (valueIntervals.isEmpty) {
      Double.NaN
    } else {
      valueIntervals.minBy { interval => interval.minValue() }.minValue()      
    }
  }
  
}