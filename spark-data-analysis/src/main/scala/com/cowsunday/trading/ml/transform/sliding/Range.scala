package com.cowsunday.trading.ml.transform.sliding

import com.cowsunday.sparkdataanalysis.data.PriceType
import com.cowsunday.sparkdataanalysis.data.PriceBar
import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.rdd.RDDFunctions._

/**
 * Range is the absolute difference between the highest and lowest specified price types for the specified period length.
 */
class Range(val lowPriceType: PriceType.Value, val highPriceType: PriceType.Value) extends SlidingTransform with Serializable {

  override def transform(priceData: RDD[PriceBar], length: Int): RDD[Double] = {
    priceData.sliding(length).map { bars =>
      val maxPrice = bars.map { bar => bar.getPrice(highPriceType) }.max
      val minPrice = bars.map { bar => bar.getPrice(lowPriceType) }.min

      math.abs(maxPrice - minPrice)
    }
  }

}