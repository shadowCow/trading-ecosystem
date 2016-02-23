package com.cowsunday.options

import org.scalatest.Assertions
import org.junit.Test
import org.junit.Before
import org.junit.After
import java.time._

class ValueIntervalUtilsTest extends Assertions {

  @Test def testComputeSlope {
    // all positive values, rising slope 
    val interval = new LinearValueInterval(1,2,5,10)
    assert(ValueIntervalUtils.computeSlope(interval) == 5)
    
    // all positive values, falling slope
    val interval2 = new LinearValueInterval(1,2,10,5)
    assert(ValueIntervalUtils.computeSlope(interval2) == -5)
    
    // all negative values, rising slope
    val interval3 = new LinearValueInterval(1,2,-10,-5)
    assert(ValueIntervalUtils.computeSlope(interval3) == 5)
    
    // all negative values, falling slope
    val interval4 = new LinearValueInterval(1,2,-5,-10)
    assert(ValueIntervalUtils.computeSlope(interval4) == -5)
    
    // all 0 values
    val interval5 = new LinearValueInterval(1,2,0,0)
    assert(ValueIntervalUtils.computeSlope(interval5) == 0)
    
    // positive values with 0, rising slope
    val interval6 = new LinearValueInterval(1,2,0,5)
    assert(ValueIntervalUtils.computeSlope(interval6) == 5)
    
    // positive values with 0, falling slope
    val interval7 = new LinearValueInterval(1,2,5,0)
    assert(ValueIntervalUtils.computeSlope(interval7) == -5)
    
    // negative values with 0, rising slope
    val interval8 = new LinearValueInterval(1,2,-5,0)
    assert(ValueIntervalUtils.computeSlope(interval8) == 5)
    
    // negative values with 0, falling slope
    val interval9 = new LinearValueInterval(1,2,0,-5)
    assert(ValueIntervalUtils.computeSlope(interval9) == -5)
    
    // crossing 0
    val interval10 = new LinearValueInterval(1,2,-2,2)
    assert(ValueIntervalUtils.computeSlope(interval10) == 4)
    
    // crossing 0 the other way
    val interval11 = new LinearValueInterval(1,2,2,-2)
    assert(ValueIntervalUtils.computeSlope(interval11) == -4)
    
    // to positive infinity
    val interval12 = new LinearValueInterval(1,2,1,Double.PositiveInfinity)
    assert(ValueIntervalUtils.computeSlope(interval12) == Double.PositiveInfinity)
    
    // to negative infinity
    val interval13 = new LinearValueInterval(1,2, -1,Double.NegativeInfinity)
    assert(ValueIntervalUtils.computeSlope(interval13) == Double.NegativeInfinity)
    
    // negative and positive infinity
    val interval14 = new LinearValueInterval(1,2,Double.NegativeInfinity, Double.PositiveInfinity)
    assert(ValueIntervalUtils.computeSlope(interval14) == Double.PositiveInfinity)
    
    val interval15 = new LinearValueInterval(1,2,Double.NegativeInfinity,-1)
    assert(ValueIntervalUtils.computeSlope(interval15) == Double.PositiveInfinity)
    
    val interval16 = new LinearValueInterval(1,2,Double.PositiveInfinity, 1)
    assert(ValueIntervalUtils.computeSlope(interval16) == Double.NegativeInfinity)
    
    val interval17 = new LinearValueInterval(1,2,Double.PositiveInfinity, Double.NegativeInfinity)
    assert(ValueIntervalUtils.computeSlope(interval17) == Double.NegativeInfinity)
  }
  
  @Test def testComputeZeroPoint {
  }
}