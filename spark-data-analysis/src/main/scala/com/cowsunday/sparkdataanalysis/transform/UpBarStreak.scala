package com.cowsunday.sparkdataanalysis.transform

import org.apache.spark.rdd.RDD
import com.cowsunday.sparkdataanalysis.data.PriceBar

/**
 * Counts the number of bars in a row where the close > open, including the current bar
 */
object UpBarStreak {

  def transform(priceData: RDD[PriceBar]): RDD[Integer] = {
    
    priceData.map(priceBar =>
      if (priceBar.isUp) {        
        1
      } else {       
        0
      })
      
  }
}