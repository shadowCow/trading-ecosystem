package com.cowsunday.sparkdataanalysis.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.rdd.RDDFunctions._
import com.cowsunday.sparkdataanalysis.data.PriceBar

object PriceChangeRank {
  
  /**
   * Ranks the last length bars by the value (close - open)
   * 
   * Smallest values (most negative) have the lowest rank (lowest rank is 0).
   * Largest values (most positive) have the highest rank (highest rank is length-1)
   */
  def transform(priceData: RDD[PriceBar], length: Integer): RDD[Integer] = {
    priceData.sliding(length).map(currentSlice => {
      val lastBar = currentSlice.takeRight(1)(0)
      currentSlice.sortBy(_.getOpenCloseChange).indexOf(lastBar)
    })
  }
}