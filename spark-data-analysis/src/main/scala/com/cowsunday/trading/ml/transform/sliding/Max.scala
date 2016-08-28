package com.cowsunday.trading.ml.transform.sliding

import com.cowsunday.sparkdataanalysis.data.PriceType
import com.cowsunday.sparkdataanalysis.data.PriceBar
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.rdd.RDDFunctions._

class Max(val priceType: PriceType.Value) extends SlidingTransform with Serializable {
  override def transform(priceData: RDD[PriceBar], length: Int): RDD[Double] = {
    priceData.sliding(length).map { bars =>
      bars.map { bar => bar.getPrice(priceType) }.max
    }
  }
}