package com.cowsunday.trading.ml.ratio

import org.apache.spark.rdd.RDD

object Ratio {

  def get(values1: RDD[Double], values2: RDD[Double]): RDD[Double] = {
    values1.zip(values2).map(pair => pair._1 / pair._2)
  }

}