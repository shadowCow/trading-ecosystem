package com.cowsunday.sparkdataanalysis.transform

import org.apache.spark.rdd.RDD
import com.cowsunday.sparkdataanalysis.data.PriceBar

object OpenCloseToHighLowRangeRatio {

  def transform(priceData: RDD[PriceBar]): RDD[Double] = {
    priceData.map { priceBar => priceBar.getOpenCloseChange / priceBar.getHighLowRange }
  }
}