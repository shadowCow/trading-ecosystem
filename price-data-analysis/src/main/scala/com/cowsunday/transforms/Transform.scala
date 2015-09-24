package com.cowsunday.transforms

trait Transform {
  def transform(dataset: Array[Double]): Array[Double]
}
