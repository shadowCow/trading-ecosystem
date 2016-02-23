package com.cowsunday.options

object ValueIntervalUtils {
  def getValueIntervals(start: Double, end: Double, valueAtStart: Double, valueAtEnd: Double): List[LinearValueInterval] = {
    val interval = new LinearValueInterval(start, end, valueAtStart, valueAtEnd)
    
    split(interval)
  }
  
  /**
   * If the end of the interval is infinity, we need to trick this thing into computing the correct slope, since infinity in the slope calculation causes an infinity slope, which is no good here.
   */
  def getValueIntervalsWithInfinity(start: Double, end: Double, valueAtStart: Double, valueAtEnd: Double, pseudoEnd: Double, pseudoValueAtEnd: Double): List[LinearValueInterval] = {
    if (end != Double.PositiveInfinity) {
      throw new IllegalArgumentException("end must equal Double.PositiveInfinity")
    }
    val interval = new LinearValueInterval(start, pseudoEnd, valueAtStart, pseudoValueAtEnd)
    
    val splitIntervals: List[LinearValueInterval] = split(interval)
    if (splitIntervals.size == 1) {
      // no need to split them, just return the interval with the end infinity values
      List(new LinearValueInterval(start, end, valueAtStart, valueAtEnd))
    } else if (splitIntervals.size == 2) {
      // the first interval ends before infinity, so keep it, adjust the second interval so that it ends at infinity
      List(splitIntervals(0), new LinearValueInterval(splitIntervals(1).start, end, splitIntervals(1).valueAtStart, valueAtEnd))
    } else {
      // wtf
      throw new IllegalStateException("splitIntervals can't possibly be a size other than 1 or 2")
    }
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
      interval.end
    } else if (interval.valueAtStart > 0 && interval.valueAtEnd > 0) {
      Double.NaN
    } else if (interval.valueAtStart < 0 && interval.valueAtEnd < 0) {
      Double.NaN
    } else {
      interval.start - (interval.valueAtStart / computeSlope(interval))
    }
  }
  
  /**
   * The goal is to split an interval into sub intervals that are only positive valued or only negative valued
   * If the current interval already fits that criteria, then we don't need to split it, and we just return the interval
   */
  def split(interval: LinearValueInterval): List[LinearValueInterval] = {
    val zeroPoint = computeZeroPoint(interval)
    if (zeroPoint.isNaN()) {
      List(interval)
    } else {
      List(new LinearValueInterval(interval.start, zeroPoint, interval.valueAtStart, 0), 
           new LinearValueInterval(zeroPoint, interval.end, 0, interval.valueAtEnd))
    }
  }
}