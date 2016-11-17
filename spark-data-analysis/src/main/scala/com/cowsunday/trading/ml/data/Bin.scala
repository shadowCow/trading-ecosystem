package com.cowsunday.trading.ml.data

/**
 * This is the same thing as a 'class' in machine learning (used for classification).
 * We use 'Bin' instead of 'class' because of the confusion that can arise with the programming construct 'class'.
 */
case class Bins(labelsAndLimits: List[(String,Double)]) {

  val bins: List[Bin] = labelsAndLimits.zipWithIndex.map(v => Bin(v._1._1, v._1._2, v._2))

  def bin(value: Double): Int = {
    bins.find(value < _.upperLimit) match {
      case Some(b) => b.bin
      case None => throw new Exception("value exceeds max bin upper limit")
    }
  }

}

case class Bin(val label: String, val upperLimit: Double, val bin: Int)