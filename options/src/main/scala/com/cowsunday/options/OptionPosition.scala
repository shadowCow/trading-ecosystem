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
    val optionLegsValue = optionLegs.map { leg => leg.getValueAtExpiration(underlyingPrice) }.sum
    
    if (stockPosition != null) {
      optionLegsValue + stockPosition.getValue(underlyingPrice)
    } else {
      optionLegsValue
    }
  }
  
  private def getValueIntervals(optionLegs: List[OptionLeg]): List[LinearValueInterval] = {
    val strikes = optionLegs.map { leg => leg.option.strikePrice }.union(List(0, Double.PositiveInfinity)).sorted
    val intervals = ListBuffer[LinearValueInterval]()
    for (i <- 1 until strikes.length) {
      intervals ++= ValueIntervalUtils.getValueIntervals(strikes(i-1), strikes(i), getValueAtExpiration(strikes(i-1)), getValueAtExpiration(strikes(i)))
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