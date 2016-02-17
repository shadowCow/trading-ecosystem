package com.cowsunday.options

object ValueIntervalUtils {
  def getValueIntervals(start: Double, end: Double, valueAtStart: Double, valueAtEnd: Double): List[LinearValueInterval] = {
    val interval = new LinearValueInterval(start, end, valueAtStart, valueAtEnd)
    
    split(interval)
  }
  
  def computeSlope(interval: LinearValueInterval): Double = {
    return (interval.valueAtEnd - interval.valueAtStart) / (interval.end - interval.start)
  }
  
  def computeZeroPoint(interval: LinearValueInterval): Double = {
    if (interval.valueAtStart == 0 && interval.valueAtEnd == 0) {
      Double.NaN
    } else if (interval.valueAtStart == 0) {
      interval.start
    } else if (interval.valueAtEnd == 0) {
      interval.start
    } else if (interval.valueAtStart > 0 && interval.valueAtEnd > 0) {
      Double.NaN
    } else if (interval.valueAtStart < 0 && interval.valueAtEnd < 0) {
      Double.NaN
    } else {
      interval.start - (interval.valueAtStart / computeSlope(interval))
    }
  }
  
  def split(interval: LinearValueInterval): List[LinearValueInterval] = {
    val zeroPoint = computeZeroPoint(interval)
    if (zeroPoint == Double.NaN) {
      List(interval)
    } else {
      List(new LinearValueInterval(interval.start, zeroPoint, interval.valueAtStart, 0), 
           new LinearValueInterval(zeroPoint, interval.end, 0, interval.valueAtEnd))
    }
  }
}