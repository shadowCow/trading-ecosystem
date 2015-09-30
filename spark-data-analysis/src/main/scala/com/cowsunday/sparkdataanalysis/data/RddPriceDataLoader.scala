package com.cowsunday.sparkdataanalysis.data

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object RddPriceDataLoader {
  def loadPriceData(sparkContext: SparkContext, file: String): RDD[PriceBar] = {
    
    val priceData = sparkContext.textFile(file).map { line => 
      val values = line.split(' ')
      val priceBar = new PriceBar(values(2).toDouble, values(3).toDouble, values(4).toDouble, values(5).toDouble, values(0).toInt)
      priceBar
    }
    
    priceData
  }
}