package com.cowsunday.sparkdataanalysis.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.rdd.RDDFunctions._
import com.cowsunday.sparkdataanalysis.data.PriceBar

object SmallestPriceChangeSeries {

  def transform(priceData: RDD[PriceBar], lengthForPriceChange: Integer, lengthForSeries: Integer): RDD[Integer] = {
    val smallestPriceChangeRDD = SmallestPriceChange.transform(priceData, lengthForPriceChange)
    
    smallestPriceChangeRDD.sliding(lengthForSeries).map { currentSlice => 
       if (currentSlice.forall { x => x == 1 }) 1 else 0 
      }
  }
  
}