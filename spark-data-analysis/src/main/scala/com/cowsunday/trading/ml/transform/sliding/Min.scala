package com.cowsunday.trading.ml.transform.sliding

import com.cowsunday.sparkdataanalysis.data.PriceType
import com.cowsunday.sparkdataanalysis.data.PriceBar
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.rdd.RDDFunctions._

class Min extends SlidingTransform with Serializable {
  override def transform(data: RDD[Double], length: Int): RDD[Double] = {
    data.sliding(length).map{ window =>
      window.min
    }
  }
}