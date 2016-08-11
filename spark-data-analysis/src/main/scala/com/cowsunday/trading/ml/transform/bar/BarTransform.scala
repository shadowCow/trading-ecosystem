package com.cowsunday.trading.ml.transform.bar

import org.apache.spark.rdd.RDD
import com.cowsunday.sparkdataanalysis.data.PriceBar

trait BarTransform {
  def transform(priceData: RDD[PriceBar]): RDD[Double]
}