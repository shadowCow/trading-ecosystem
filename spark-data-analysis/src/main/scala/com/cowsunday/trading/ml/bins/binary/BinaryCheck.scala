package com.cowsunday.trading.ml.bins.binary

import org.apache.spark.rdd.RDD
import com.cowsunday.trading.ml.data.BinaryVariable

trait BinaryCheck {
  def transform(data1: RDD[Double], data2: RDD[Double]): RDD[BinaryVariable]
}