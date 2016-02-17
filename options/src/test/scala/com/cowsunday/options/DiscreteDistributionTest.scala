package com.cowsunday.options

import org.scalatest.Assertions
import org.junit.Test
import org.junit.Before
import org.junit.After

class DiscreteDistributionTest extends Assertions {

  @Test def testDistribution() {
    val points = List[DiscreteDistributionPoint](DiscreteDistributionPoint(1,0.25), DiscreteDistributionPoint(2, 0.25), DiscreteDistributionPoint(3, 0.25), DiscreteDistributionPoint(4, 0.25))
    
    val distribution = new DiscreteDistribution(points)
    
    assert(distribution.cumulativeProbability(1.5) == 0.25)
    assert(distribution.cumulativeProbability(2.0) == 0.5)
    assert(distribution.cumulativeProbability(0) == 0)
    assert(distribution.cumulativeProbability(4.0) == 1.0)
    assert(distribution.cumulativeProbability(10.0) == 1.0)
    
    assert(distribution.probability(0.5) == 0)
    assert(distribution.probability(1.0) == 0.25)
  }
}