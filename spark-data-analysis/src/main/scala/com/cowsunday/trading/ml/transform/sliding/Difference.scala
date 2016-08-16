package com.cowsunday.trading.ml.transform.sliding

import com.cowsunday.sparkdataanalysis.data.PriceType
import com.cowsunday.sparkdataanalysis.data.PriceBar
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.rdd.RDDFunctions._

/**
 * Take the difference of the last and first bars, using the specified price types
 */
class Difference(val firstPriceType: PriceType.Value, val lastPriceType: PriceType.Value) extends SlidingTransform with Serializable {

  override def transform(priceData: RDD[PriceBar], length: Int): RDD[Double] = {
    priceData.sliding(length).map { bars =>
      bars.last.getPrice(lastPriceType) - bars.head.getPrice(firstPriceType)
    }
  }

}