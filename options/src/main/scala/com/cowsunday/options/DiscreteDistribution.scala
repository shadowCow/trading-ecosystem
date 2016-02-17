package com.cowsunday.options

class DiscreteDistribution(val points: List[DiscreteDistributionPoint]) extends Distribution {
  
  if (points.map { point => point.value }.distinct.length != points.length) {
    throw new IllegalArgumentException("values for this distribution must be distinct")
  }
  
  if (points.map { point => point.probability }.sum != 1.0) {
    throw new IllegalArgumentException("sum of the probabilities for this distribution must equal 1.0")
  }
  
  def cumulativeProbability(x: Double): Double = {
    this.points.takeWhile { point => point.value <= x }.map { point => point.probability }.sum
  }
  
  def probability(x: Double): Double = {
    this.points.find { point => point.value == x }.map { point => point.probability }.sum
  }
}