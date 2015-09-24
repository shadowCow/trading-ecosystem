package com.cowsunday.transforms

object TransformRunner {
  def main(args: Array[String]) {
    var midpoint = new Midpoint()

    var data = Array(1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0)
    var data2 = Array(2.0,3,4,5,6,7,8,9,10,11)

    var transformed = midpoint.combine(data, data2)

    transformed.foreach {println}

  }
}
