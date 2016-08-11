package com.cowsunday.trading.ml.transform.sliding

import org.apache.spark.rdd.RDD
import com.cowsunday.sparkdataanalysis.data.PriceBar

trait SlidingTransform {
  def transform(priceData: RDD[PriceBar], length: Integer): RDD[Double]
}