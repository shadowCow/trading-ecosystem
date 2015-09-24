package com.cowsunday.sparkdataanalysis

import org.apache.spark.{ SparkConf, SparkContext }
import org.apache.spark.mllib.linalg.distributed.RowMatrix
import org.apache.spark.mllib.linalg.Vectors

/**
 * @author dwadeson
 */
object MyPca {

  def main(args: Array[String]) {
    if (args.length != 1) {
      System.err.println("Usage: TallSkinnyPCA <input>")
      System.exit(1)
    }
    val conf = new SparkConf().setAppName("TallSkinnyPCA")
    val sc = new SparkContext(conf)

    val rows = sc.textFile(args(0)).map { line =>
      val values = line.split(' ').map(_.toDouble)
      Vectors.dense(values)
    }
    val mat = new RowMatrix(rows)

    val pc = mat.computePrincipalComponents(mat.numCols().toInt)

    println("Principal components are: \n" + pc)
    sc.stop()
  }

}