package com.cowsunday.trading.ml.transform.sliding

import com.cowsunday.sparkdataanalysis.data.PriceType
import com.cowsunday.sparkdataanalysis.data.PriceBar
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.rdd.RDDFunctions._

class AbsoluteDifference(val firstPriceType: PriceType.Value, val lastPriceType: PriceType.Value) extends SlidingTransform with Serializable {

  override def transform(priceData: RDD[PriceBar], length: Int): RDD[Double] = {
    priceData.sliding(length).map { bars =>
      math.abs(bars.last.getPrice(lastPriceType) - bars.head.getPrice(firstPriceType))
    }
  }

}