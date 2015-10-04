package com.cowsunday.sparkdataanalysis.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.rdd.RDDFunctions._
import com.cowsunday.sparkdataanalysis.data.PriceBar

object HiLowRangeRank {

  def transform(priceData: RDD[PriceBar], length: Integer): RDD[Integer] = {
    priceData.sliding(length).map(currentSlice => {
      val lastBar = currentSlice.takeRight(1)(0)
      currentSlice.sortBy(_.getHighLowRange).indexOf(lastBar)
    })
  }
}