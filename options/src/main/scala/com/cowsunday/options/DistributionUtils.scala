package com.cowsunday.options

object DistributionUtils {

  def discreteDistribution(points: List[Tuple2[Double, Double]]): DiscreteDistribution = {
    new DiscreteDistribution(points.map { point => DiscreteDistributionPoint(point._1,point._2) })
  }
  
}