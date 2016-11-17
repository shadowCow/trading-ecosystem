package com.cowsunday.trading.ml

import com.cowsunday.trading.ml.transform._
import com.cowsunday.trading.ml.transform.bar._
import com.cowsunday.trading.ml.transform.sliding._
import com.cowsunday.trading.ml.feature._
import com.cowsunday.trading.ml.data._
import org.apache.spark.rdd.RDD

class Runner {
  val barTransforms = new BarTransforms().all

  val slidingTransforms = new SlidingTransforms().allDouble

  val lengths = (2 to 20).toList

  val transforms = new Transforms()

  val featureSelector = new FeatureSelector(barTransforms, slidingTransforms, lengths, 0, 0, 0, transforms)

  // dummy data so this thing works
  val opens = Array[Double](2,3,4,5)
  val highs = Array[Double](4,5,6,7)
  val lows = Array[Double](1,2,3,4)
  val closes = Array[Double](3,4,5,6)
  val dates = Array[Int](20010101,20010102,20010103,20010104)
  //val priceData = RddPriceDataLoader.loadPriceData(sparkContext, file)

//  try {
//    // keep going till an exception is thrown for end of feature list
//    while (true) {
//      val feature = featureSelector.nextFeature(priceData)
//    }
//  } catch {
//    case e: EndOfFeatureListException => {
//
//    }
//  }
}