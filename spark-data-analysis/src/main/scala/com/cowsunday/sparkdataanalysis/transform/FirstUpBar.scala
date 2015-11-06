package com.cowsunday.sparkdataanalysis.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.rdd.RDDFunctions._
import com.cowsunday.sparkdataanalysis.data.PriceBar

object FirstUpBar {

  def transform(priceData: RDD[PriceBar], length: Integer): RDD[Integer] = {
    priceData.sliding(length).map(currentSlice => {
      
      val firstUpIndex = currentSlice.indexWhere { x => x.isUp }
      
      if (firstUpIndex == currentSlice.length-1) 1 else 0
    });
  }
}