package com.cowsunday.transforms

trait Combiner {
  def combine(dataset1: Array[Double], dataset2: Array[Double]): Array[Double]
}
