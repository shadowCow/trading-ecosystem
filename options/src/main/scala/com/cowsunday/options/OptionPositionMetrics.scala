package com.cowsunday.options

import org.apache.commons.math3.distribution._
import org.apache.commons.math3.analysis._
import org.apache.commons.math3.analysis.integration._

object OptionPositionMetrics {

  def computeMetrics(optionPosition: OptionPosition, distribution: RealDistribution): PositionMetrics = {
    val winPct = optionPosition.positiveValueIntervals.map { interval => 
      distribution.cumulativeProbability(interval.end) - distribution.cumulativeProbability(interval.start)
      }.sum
    
    val expectedGain = computeCumulativeExpectation(optionPosition.positiveValueIntervals, optionPosition, distribution)
    val expectedLoss = computeCumulativeExpectation(optionPosition.negativeValueIntervals, optionPosition, distribution)
    
    val rr = computeRewardRiskRatio(expectedGain, expectedLoss)
    val expectedValue = computeExpectedValue(rr, winPct)
    
    new PositionMetrics(expectedValue, winPct, rr, optionPosition.maxGain, optionPosition.maxLoss)
  }
  
  def computeCumulativeExpectation(valueIntervals: List[LinearValueInterval], optionPosition: OptionPosition, distribution: RealDistribution): Double = {
      
    val integrator = new IterativeLegendreGaussIntegrator(2, 5, 100)
    val expectedValueFunction = new UnivariateFunction() {
        override def value(assetPrice: Double): Double = {
          optionPosition.getValueAtExpiration(assetPrice) * distribution.probability(assetPrice)
        }
      }
      
    val expectedValue = valueIntervals.map { interval => 
        integrator.integrate(100, expectedValueFunction, interval.start, interval.end)
      }.sum
    
    expectedValue
  }
  
  def computeRewardRiskRatio(expectedGain: Double, expectedLoss: Double): Double = {
    if (expectedGain < 0 || expectedLoss < 0) {
      throw new IllegalArgumentException("expectedGain and expectedLoss must be >= 0")
    } else if (expectedLoss == 0.0 && expectedGain == 0.0) {
      Double.NaN
    } else if (expectedLoss == 0.0) {
      Double.PositiveInfinity
    } else {
      expectedGain / expectedLoss
    }
  }
  
  def computeExpectedValue(rewardRiskRatio: Double, winPct: Double): Double = {
    rewardRiskRatio * winPct - (1 - winPct)
  }
}