package com.cowsunday.sparkdataanalysis.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.rdd.RDDFunctions._
import com.cowsunday.sparkdataanalysis.data.PriceBar

object BiggerThanMultiBarRange {

  def transform(priceData: RDD[PriceBar], length: Integer): RDD[Integer] = {
    priceData.sliding(length+1).map(currentSlice => {
      val rangeSlice = currentSlice.take(length)
      val lowestBar = rangeSlice.sortBy(_.getLow).take(1)(0)
      val highestBar = rangeSlice.sortBy(_.getHigh).takeRight(1)(0)
      
      val range = highestBar.getHigh - lowestBar.getLow
      
      val currentBar = currentSlice.takeRight(1)(0)
      if (currentBar.getHighLowRange > range) 1 else 0
    })
  }
}