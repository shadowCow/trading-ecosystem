package com.cowsunday.trading.ml.feature

import com.cowsunday.sparkdataanalysis.data._
import com.cowsunday.trading.ml.transform.bar._
import com.cowsunday.trading.ml.transform.sliding._
import org.apache.spark.rdd.RDD

class FeatureSelector(val initialBarTransformIndex: Int, val initialSlidingTransformIndex: Int, val initialLengthIndex: Int) {

  val barTransforms = List(
      new Open(),
      new High(),
      new Low(),
      new Close(),
      new OpenCloseChange(),
      new OpenCloseRange(),
      new OpenHighRange(),
      new OpenLowRange(),
      new HiLowRange(),
      new CloseHighRange(),
      new CloseLowRange()
      )

  val slidingTransforms: Seq[SlidingTransform] = Seq()
//    {for (p <- PriceType.values) yield new Min(p)} ++
//    {for (p <- PriceType.values) yield new Max(p)} ++
//    {for (p <- PriceType.values) yield new Difference(p,p)} ++
//    {for (p <- PriceType.values) yield new AbsoluteDifference(p,p)} ++
//    {for (p <- PriceType.values) yield new Range(p,p)}

  val lengths = (2 to 20).toList

  var barTransformIndex = 0
  var slidingTransformIndex = 0
  var lengthIndex = 0

  private def nextBarFeature(priceData: RDD[PriceBar]): RDD[Double] = {
    if (barTransformIndex < barTransforms.length) {
      val nextFeature = barTransforms(barTransformIndex)

      barTransformIndex = barTransformIndex + 1
      nextFeature.transform(priceData)
    } else {
      throw new Exception()
    }
  }

  private def nextSlidingFeature(data: RDD[Double]): RDD[Double] = {
    if (slidingTransformIndex < slidingTransforms.length) {
      if (lengthIndex < lengths.length) {
        val nextSliding = slidingTransforms(slidingTransformIndex)
        val nextFeature = nextSliding.transform(data, lengths(lengthIndex))

        lengthIndex = lengthIndex + 1
        nextFeature
      } else {
        lengthIndex = 0
        slidingTransformIndex = slidingTransformIndex + 1

        val nextSliding = slidingTransforms(slidingTransformIndex)
        val nextFeature = nextSliding.transform(data, lengths(lengthIndex))

        lengthIndex = lengthIndex + 1
        nextFeature
      }

    } else {
      throw new Exception()
    }

  }

}