package com.cowsunday.sparkdataanalysis.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.rdd.RDDFunctions._
import com.cowsunday.sparkdataanalysis.data.PriceBar

trait SlidingTransform {

  def transform(priceData: RDD[PriceBar], length: Integer): RDD[Integer]
  
}