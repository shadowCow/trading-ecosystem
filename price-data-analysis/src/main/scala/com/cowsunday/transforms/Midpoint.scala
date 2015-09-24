package com.cowsunday.transforms

class Midpoint extends Combiner {
  override def combine(dataset1: Array[Double], dataset2: Array[Double]): Array[Double] = {
    require(dataset1.length == dataset2.length)

    var combined = new Array[Double](dataset1.length)
    for ( x <- 0 until dataset1.length ){
        combined(x) = (dataset1(x) + dataset2(x)) / 2
    }

    combined
  }
}
