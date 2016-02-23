package com.cowsunday.options

import org.scalatest.Assertions
import org.junit.Test
import org.junit.Before
import org.junit.After
import java.time._

class LinearValueIntervalTest extends Assertions {
  
  @Test def testIntervals() {
    // all positive
    val interval1 = new LinearValueInterval(10, 20, 5, 10)
    assert(interval1.isPositiveValuesOnly())
    assert(!interval1.isNegativeValuesOnly())
    assert(!interval1.isZeroValuesOnly())
    assert(interval1.maxValue() == 10)
    assert(interval1.minValue() == 5)
    
    // all negative
    val interval2 = new LinearValueInterval(-5, -10, -2, -3)
    assert(!interval2.isPositiveValuesOnly())
    assert(interval2.isNegativeValuesOnly())
    assert(!interval2.isZeroValuesOnly())
    assert(interval2.maxValue() == -2)
    assert(interval2.minValue() == -3)
    
    // all 0
    val interval3 = new LinearValueInterval(1, 2, 0, 0)
    assert(!interval3.isPositiveValuesOnly())
    assert(!interval3.isNegativeValuesOnly())
    assert(interval3.isZeroValuesOnly())
    assert(interval3.maxValue() == 0)
    assert(interval3.minValue() == 0)
    
    // crossing 0
    val interval4 = new LinearValueInterval(-2, 5, -1, 2)
    assert(!interval4.isPositiveValuesOnly())
    assert(!interval4.isNegativeValuesOnly())
    assert(!interval4.isZeroValuesOnly())
    assert(interval4.maxValue == 2)
    assert(interval4.minValue == -1)
    
    // positive with 0
    val interval5 = new LinearValueInterval(3, 4, 0, 2)
    assert(interval5.isPositiveValuesOnly())
    assert(!interval5.isNegativeValuesOnly())
    assert(!interval5.isZeroValuesOnly())
    assert(interval5.maxValue() == 2)
    assert(interval5.minValue() == 0)
    
    // negative with 0
    val interval6 = new LinearValueInterval(12, 24, -3, 0)
    assert(!interval6.isPositiveValuesOnly())
    assert(interval6.isNegativeValuesOnly())
    assert(!interval6.isZeroValuesOnly())
    assert(interval6.maxValue() == 0)
    assert(interval6.minValue() == -3)
  }
}