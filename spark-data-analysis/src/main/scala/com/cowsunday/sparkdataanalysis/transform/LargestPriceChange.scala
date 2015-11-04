package com.cowsunday.sparkdataanalysis.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.rdd.RDDFunctions._
import com.cowsunday.sparkdataanalysis.data.PriceBar

object LargestPriceChange {
  
  def transform(priceData: RDD[PriceBar], length: Integer): RDD[Integer] = {
    priceData.sliding(length).map(currentSlice => {
      val lastBar = currentSlice.takeRight(1)(0)
      val largestBar = currentSlice.sortBy(_.getOpenCloseChange).takeRight(1)(0)
      
      if (lastBar == largestBar) 1 else 0
    });
  }
}