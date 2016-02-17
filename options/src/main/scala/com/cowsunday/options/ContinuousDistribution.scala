package com.cowsunday.options

import org.apache.commons.math3.distribution._

class ContinuousDistribution(val distribution: RealDistribution) extends Distribution {

  def cumulativeProbability(x: Double): Double = {
    distribution.cumulativeProbability(x)
  }
  
  def probability(x: Double): Double = {
    distribution.probability(x)
  }
  
}