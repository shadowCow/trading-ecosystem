package com.cowsunday.options

import org.apache.commons.math3.distribution._
import org.apache.commons.math3.analysis._
import org.apache.commons.math3.analysis.integration._

object OptionPositionMetrics {

  def computeMetrics(optionPosition: OptionPosition, distribution: DiscreteDistribution): PositionMetrics = {
    val winPct = optionPosition.positiveValueIntervals.map { interval => 
      distribution.cumulativeProbability(interval.end) - distribution.cumulativeProbability(interval.start)
      }.sum
    
    val positiveValuePoints = getPositiveValuePoints(optionPosition.positiveValueIntervals, distribution)
    val negativeValuePoints = getNegativeValuePoints(optionPosition.negativeValueIntervals, distribution)
    
    val expectedGain = computeCumulativeExpectation(positiveValuePoints, optionPosition, winPct)
    val expectedLoss = computeCumulativeExpectation(negativeValuePoints, optionPosition, 1 - winPct)

    // multiply expectedLoss by -1 so that we are getting a positive ratio
    val rr = computeRewardRiskRatio(expectedGain, expectedLoss * (-1))
    val expectedValue = computeExpectedValue(rr, winPct)
    
    new PositionMetrics(expectedValue, winPct, rr, optionPosition.maxGain, optionPosition.maxLoss)
  }
  
  private def getPositiveValuePoints(valueIntervals: List[LinearValueInterval], distribution: DiscreteDistribution): List[DiscreteDistributionPoint] = {
    distribution.points.filter { point => valueIntervals.exists { interval => interval.contains(point.value) } }
  }
  
  private def getNegativeValuePoints(valueIntervals: List[LinearValueInterval], distribution: DiscreteDistribution): List[DiscreteDistributionPoint] = {
    distribution.points.filter { point => valueIntervals.exists { interval => interval.contains(point.value) } }
  }
  
  /**
   * @param adjustmentFactor we need to normalize the distribution here so that the sum of the probabilities is 1.
   */
  private def computeCumulativeExpectation(points: List[DiscreteDistributionPoint], optionPosition: OptionPosition, adjustmentFactor: Double): Double = {
    points.map { point => optionPosition.getNetGainAtExpiration(point.value) * point.probability }.sum / adjustmentFactor
  }
  
  private def computeRewardRiskRatio(expectedGain: Double, expectedLoss: Double): Double = {
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
  
  private def computeExpectedValue(rewardRiskRatio: Double, winPct: Double): Double = {
    rewardRiskRatio * winPct - (1 - winPct)
  }
}