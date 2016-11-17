package com.cowsunday.trading.ml.bins

import org.apache.spark.rdd.RDD

/**
 * Class for comparing two RDDs index by index to determine which value is greater at each index.
 *
 * The resulting RDD will only have as many values as the shorter of the two input RDDs
 */
class Comparison {
  def compare(series1: RDD[Double], series2: RDD[Double]): RDD[Int] = {
    series1.zip(series2).map { zipped =>
      zipped._1.compareTo(zipped._2)
    }
  }
}