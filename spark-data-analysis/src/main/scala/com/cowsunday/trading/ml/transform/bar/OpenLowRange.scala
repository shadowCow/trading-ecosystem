package com.cowsunday.trading.ml.transform.bar

import org.apache.spark.rdd.RDD
import com.cowsunday.sparkdataanalysis.data.PriceBar

class OpenLowRange extends BarTransform {
  override def transform(priceData: RDD[PriceBar]): RDD[Double] = {
    priceData.map { bar => bar.getOpen - bar.getLow }
  }
}