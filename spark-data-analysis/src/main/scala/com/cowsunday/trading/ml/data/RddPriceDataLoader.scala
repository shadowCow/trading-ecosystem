package com.cowsunday.trading.ml.data

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

object RddPriceDataLoader {

  val datePattern = """(\d\d\d\d\d\d\d\d).*""".r

  def loadPriceData(sparkContext: SparkContext, file: String): RDD[PriceBar] = {

    sparkContext.textFile(file).filter {
      line =>
        line match {
          case datePattern(_*) => true
          case _ => false
        }
    }.map { line =>
      val values = line.split(' ')
      new PriceBar(values(2).toDouble, values(3).toDouble, values(4).toDouble, values(5).toDouble, values(0).toInt)
    }

  }
}