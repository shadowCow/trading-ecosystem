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
  
  def getValueAtExpiration(underlyingPrice: Double): Double = {
    val optionLegsValue = getOptionLegsValue(optionLegs, underlyingPrice)
    
    if (stockPosition != null) {
      optionLegsValue + stockPosition.getValue(underlyingPrice)
    } else {
      optionLegsValue
    }
  }
  
  /**
   * We need to deal with off-setting options before we sum up values, otherwise we might be adding positive and negative infinity together, which is undefined.
   */
  private def getOptionLegsValue(optionLegs: List[OptionLeg], underlyingPrice: Double): Double = {
    if (underlyingPrice == Double.PositiveInfinity) {
      // if the underlying price is positive infinity, we have to account for off-setting calls
      val longCalls = optionLegs.filter { leg => leg.direction == PositionDirection.LONG && leg.option.optionType == OptionType.CALL }
      val shortCalls = optionLegs.filter { leg => leg.direction == PositionDirection.SHORT && leg.option.optionType == OptionType.CALL }
      val longCallQuantity = longCalls.map { leg => leg.quantity }.sum
      val shortCallQuantity = shortCalls.map { leg => leg.quantity }.sum
      
      if (longCallQuantity > shortCallQuantity) {
        // at least one call will be positive infinity, and all shorts will be offset, so its just positive infinity
        Double.PositiveInfinity
      } else if (longCallQuantity < shortCallQuantity){
        // at least one call will be negative infinity, and all longs will be offset, so its just negative infinity
        Double.NegativeInfinity
      } else {
        // since we know the call quantities offset, we can do a neat trick by using strike values and quantities to get their net value
        val longCallStrikeVals = longCalls.map { call => call.quantity * call.option.strikePrice * -1 }.sum
        val shortCallStrikeVals = shortCalls.map { call => call.quantity * call.option.strikePrice }.sum
        val putValues = optionLegs.filter { leg => leg.option.optionType == OptionType.PUT }.map { leg => leg.getValueAtExpiration(underlyingPrice) }.sum
        
        longCallStrikeVals + shortCallStrikeVals + putValues
      }
    } else {
      // if underlying price is not positive infinity, then all legs have finite value, and we can just do a simple sum
      optionLegs.map { leg => leg.getValueAtExpiration(underlyingPrice) }.sum
    }
  }
  
  private def getValueIntervals(optionLegs: List[OptionLeg]): List[LinearValueInterval] = {
    val strikes = optionLegs.map { leg => leg.option.strikePrice }.union(List(0, Double.PositiveInfinity)).distinct.sorted
    
    val intervals = ListBuffer[LinearValueInterval]()
    for (i <- 1 until strikes.length) {
      if (strikes(i) == Double.PositiveInfinity) {
        intervals ++= ValueIntervalUtils.getValueIntervalsWithInfinity(strikes(i-1), strikes(i), getValueAtExpiration(strikes(i-1)), getValueAtExpiration(strikes(i)), strikes(i-1)*2, getValueAtExpiration(strikes(i-1)*2))
      } else {
        intervals ++= ValueIntervalUtils.getValueIntervals(strikes(i-1), strikes(i), getValueAtExpiration(strikes(i-1)), getValueAtExpiration(strikes(i))) 
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