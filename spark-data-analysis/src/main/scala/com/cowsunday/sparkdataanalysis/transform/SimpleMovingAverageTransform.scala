package com.cowsunday.sparkdataanalysis.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.rdd.RDDFunctions._
import com.cowsunday.sparkdataanalysis.data.PriceBar
import com.cowsunday.sparkdataanalysis.data.PriceType

object SimpleMovingAverageTransform {

  def transform(priceData: RDD[PriceBar], length: Integer, priceType: PriceType.Value): RDD[Double] = {
    priceData.sliding(length).map(currentSlice => currentSlice.map[Double, Array[Double]](pb => pb.getPrice(priceType)).sum / currentSlice.size) 
  }
}