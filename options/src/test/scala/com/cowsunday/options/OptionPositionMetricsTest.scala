package com.cowsunday.options

import org.scalatest.Assertions
import org.junit.Test
import org.junit.Before
import org.junit.After
import java.time._
import org.apache.commons.math3.distribution._

class OptionPositionMetricsTest extends ExtendedAssertions {
  
  private def createDistribution(): DiscreteDistribution = {
    val points = List((90.0, 0.2), (95.0, 0.2), (100.0, 0.2), (105.0, 0.2), (110.0, 0.2))
    DistributionUtils.discreteDistribution(points)
  }

  @Test def testlongCall() {
    val position = OptionPositionFactory.longCall("spy", 100, Instant.now, 2, 10)
    
    val points = List((90.0, 0.2), (95.0, 0.2), (100.0, 0.2), (105.0, 0.2), (110.0, 0.2))
    val distribution = DistributionUtils.discreteDistribution(points)
    
    val metrics = OptionPositionMetrics.computeMetrics(position, distribution)
    
    assertEquals(0.5, metrics.expectedValue, 0.0001)
    assertEquals(0.4, metrics.winPct, 0.0001)
    assertEquals(2.75, metrics.rr, 0.0001)
    
  }
  
  @Test def testShortCall() {
    val position = OptionPositionFactory.shortCall("spy", 100, Instant.now, 2, 10)
    
    val points = List((90.0, 0.2), (95.0, 0.2), (100.0, 0.2), (105.0, 0.2), (110.0, 0.2))
    val distribution = DistributionUtils.discreteDistribution(points)
    
    val metrics = OptionPositionMetrics.computeMetrics(position, distribution)
    
    assertEquals(-0.1818, metrics.expectedValue, 0.0001)
    assertEquals(0.6, metrics.winPct, 0.0001)
    assertEquals(0.3636, metrics.rr, 0.0001)
    
  }
  
  @Test def testLongPut() {
    val position = OptionPositionFactory.longPut("spy", 100, Instant.now, 2, 10)
    
    val distribution = this.createDistribution()
    
    val metrics = OptionPositionMetrics.computeMetrics(position, distribution)
    
    assertEquals(0.5, metrics.expectedValue, 0.0001)
    assertEquals(0.4, metrics.winPct, 0.0001)
    assertEquals(2.75, metrics.rr, 0.0001)
  }
  
  @Test def testShortPut() {
    val position = OptionPositionFactory.shortPut("spy", 100, Instant.now, 2, 10)
    
    val distribution = this.createDistribution()
    
    val metrics = OptionPositionMetrics.computeMetrics(position, distribution)
    
    assertEquals(-0.1818, metrics.expectedValue, 0.0001)
    assertEquals(0.6, metrics.winPct, 0.0001)
    assertEquals(0.3636, metrics.rr, 0.0001)
  }
  
  @Test def testBullCall() {
    val position = OptionPositionFactory.bullCallSpread("spy", 100, 105, Instant.now, 2, 1, 10)
    
    val distribution = this.createDistribution()
    
    val metrics = OptionPositionMetrics.computeMetrics(position, distribution)
    
    assertEquals(1.0, metrics.expectedValue, 0.0001)
    assertEquals(0.4, metrics.winPct, 0.0001)
    assertEquals(4.0, metrics.rr, 0.0001)
  }
  
  @Test def testBearCall() {
    val position = OptionPositionFactory.bearCallSpread("spy", 100, 105, Instant.now, 2, 1, 10)
    
    val distribution = this.createDistribution()
    
    val metrics = OptionPositionMetrics.computeMetrics(position, distribution)
    
    assertEquals(-0.25, metrics.expectedValue, 0.0001)
    assertEquals(0.6, metrics.winPct, 0.0001)
    assertEquals(0.25, metrics.rr, 0.0001)
  }
  
  @Test def testLongCallButterfly() {
    val position = OptionPositionFactory.longCallButterfly("spy", 95, 100, 105, Instant.now, 6, 3, 0.5, 10)
    
    val distribution = this.createDistribution()
    
    val metrics = OptionPositionMetrics.computeMetrics(position, distribution)
    
    assertEquals(1, metrics.expectedValue, 0.0001)
    assertEquals(0.2, metrics.winPct, 0.0001)
    assertEquals(9, metrics.rr, 0.0001)
  }
  
}