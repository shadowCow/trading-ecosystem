package com.cowsunday.sparkdataanalysis.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.rdd.RDDFunctions._
import com.cowsunday.sparkdataanalysis.data.PriceBar

object MultiBarHighLowRange {

  def transform(priceData: RDD[PriceBar], length: Integer): RDD[Double] = {
    priceData.sliding(length).map(currentSlice => {
      val lowestBar = currentSlice.sortBy(_.getLow).take(1)(0)
      val highestBar = currentSlice.sortBy(_.getHigh).takeRight(1)(0)
      
      highestBar.getHigh - lowestBar.getLow
    })
  }
}