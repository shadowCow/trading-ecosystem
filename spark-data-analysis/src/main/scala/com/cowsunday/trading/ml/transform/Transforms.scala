package com.cowsunday.trading.ml.transform

import org.apache.spark.rdd.RDD
import org.apache.spark.mllib.rdd.RDDFunctions._
import com.cowsunday.trading.ml.data._

class Transforms {
  def transformPriceData(priceData: RDD[PriceBar])(f: PriceBar => Double): RDD[Double] = {
    priceData.map(f(_))
  }

  def transformPriceDataWindow(priceData: RDD[PriceBar], length: Int)(f: Array[PriceBar] => Double): RDD[Double] = {
    priceData.sliding(length).map(f(_))
  }

  def transform(values: RDD[Double])(f: Double => Double): RDD[Double] =
    values.map(f(_))

  def transformWindow(values: RDD[Double], length: Int)(f: Array[Double] => Double): RDD[Double] =
    values.sliding(length).map(f(_))

  def transformToBinaryVariable(values: RDD[Double])(f: Double => Boolean): RDD[BinaryVariable] =
    values.map(v => BinaryVariable(f(v)))

  def transformWindowToBinaryVariable(values: RDD[Double], length: Int)(f: Array[Double] => Boolean): RDD[BinaryVariable] =
    values.sliding(length).map(v => BinaryVariable(f(v)))

  def lastValueComparedToWindow(values: RDD[Double], length: Int)(f: (Array[Double], Double) => Boolean): RDD[BinaryVariable] =
    values.sliding(length).map { vs =>
      val windowMinusLast = vs.dropRight(1)
      val last = vs.last
      BinaryVariable(f(windowMinusLast, last))
    }

  def transformWindowToCount(binaryValues: RDD[BinaryVariable], length: Int): RDD[Int] =
    binaryValues.sliding(length).map(bvArray =>
      bvArray.map(bv => bv.value).sum)

  def bin(values: RDD[Double], bins: Bins): RDD[Int] =
    values.map(bins.bin(_))
}

/**
 *  For cross sectional stuff
 */
class MultiMarketTransforms {

}
