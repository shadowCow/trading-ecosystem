package com.cowsunday.trading.ml.feature

import com.cowsunday.trading.ml.data._
import com.cowsunday.trading.ml.transform._
import com.cowsunday.trading.ml.transform.bar._
import com.cowsunday.trading.ml.transform.sliding._
import org.apache.spark.rdd.RDD

case class EndOfFeatureListException() extends Exception("All features have been generated")

class FeatureSelector(val barTransforms: Seq[PriceBar => Double],
    val slidingTransforms: Seq[Array[Double] => Double],
    val lengths: Seq[Int],
    val initialBarTransformIndex: Int,
    val initialSlidingTransformIndex: Int,
    val initialLengthIndex: Int,
    val transforms: Transforms) {

  def this(barTransforms: Seq[PriceBar => Double], slidingTransforms: Seq[Array[Double] => Double], lengths: Seq[Int], transforms: Transforms) {
    this(barTransforms, slidingTransforms, lengths, 0, 0, 0, transforms)
  }

  var barTransformIndex = initialBarTransformIndex
  var slidingTransformIndex = initialSlidingTransformIndex
  var lengthIndex = initialLengthIndex

  def nextFeature(priceData: RDD[PriceBar]): RDD[Double] = {
    if (barTransformIndex < barTransforms.length) {
      if (slidingTransformIndex < slidingTransforms.length) {
        if (lengthIndex < lengths.length) {
          val feature = getFeature(barTransformIndex, slidingTransformIndex, lengthIndex, priceData)
          lengthIndex += 1

          feature
        } else {
          lengthIndex = 0
          slidingTransformIndex += 1

          nextFeature(priceData)
        }
      } else {
        slidingTransformIndex = 0
        barTransformIndex += 1

        nextFeature(priceData)
      }
    } else {
      throw new EndOfFeatureListException()
    }
  }

  def getFeature(barTransformIndex: Int, slidingTransformIndex: Int, lengthIndex: Int, priceData: RDD[PriceBar]): RDD[Double] = {
    val barTransform = barTransforms(barTransformIndex)
    val slidingTransform = slidingTransforms(slidingTransformIndex)
    val length = lengths(lengthIndex)

    transforms.transformWindow(transforms.transformPriceData(priceData)(barTransform), length)(slidingTransform)
  }

}