package com.cowsunday.options

/**
 * This represents an interval where the value of the option position is linear.
 * That is, you can draw a straight line from the start of the interval to the end of the interval, 
 * where each y value on the line describes the position value, and the x value is the price.
 * 
 */
class LinearValueInterval(val start: Double, val end: Double, val valueAtStart: Double, val valueAtEnd: Double) {
  def isPositiveValuesOnly(): Boolean = {
    (valueAtStart >= 0 && valueAtEnd >= 0) && !isZeroValuesOnly()
  }
  
  def isNegativeValuesOnly(): Boolean = {
    (valueAtStart <= 0 && valueAtEnd <= 0) && !isZeroValuesOnly()
  }
  
  def isZeroValuesOnly(): Boolean = {
    valueAtStart == 0 && valueAtEnd == 0
  }
  
  def maxValue(): Double = {
    math.max(valueAtStart, valueAtEnd)
  }
  
  def minValue(): Double = {
    math.min(valueAtStart, valueAtEnd)
  }
  
  override def toString(): String = {
    "{\"start\":\"" + start + "\",\"end\":\"" + end + "\",\"valueAtStart\":\"" + valueAtStart + "\",\"valueAtEnd\":\"" + valueAtEnd + "\"}"
  }
}

