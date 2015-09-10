package com.cowsunday.analysis

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.linalg.distributed.RowMatrix
import org.apache.spark.mllib.linalg.Vectors

object ComputeSVD {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("ComputeSVD")
    val sc = new SparkContext(conf)

    val rows = "0.5 1.0\n2.0 3.0\n4.0 5.0".map { line =>
      val values = line.split(' ').map(_.toDouble)
      Vectors.dense(values)
                                                }

    val mat = new RowMatrix(rows)

    val svd = mat.computeSVD(mat.numCols().toInt)

    println("Singular values are " + svd.s)

    sc.stop()
  }
}
