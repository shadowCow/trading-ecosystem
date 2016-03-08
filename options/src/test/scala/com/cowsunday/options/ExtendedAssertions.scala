package com.cowsunday.options

import org.scalatest.Assertions

class ExtendedAssertions extends Assertions {
  
  def assertEquals(expected: Double, actual: Double, tolerance: Double) {
    assert(tolerance >= 0, "tolerance must be >= 0, but was: " + tolerance)
    assert(expected > actual - tolerance && expected < actual + tolerance, "expected: " + expected + " actual: " + actual)
  }
}