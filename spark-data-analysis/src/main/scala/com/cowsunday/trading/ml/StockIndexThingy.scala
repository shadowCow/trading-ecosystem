package com.cowsunday.trading.ml

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.mllib.rdd.RDDFunctions._

case class StockBar(val date: String,
                    val open: Double,
                    val high: Double,
                    val low: Double,
                    val close: Double,
                    val volume: Long,
                    val adjustedClose: Double) extends Serializable {}

class StockIndexThingy extends Serializable {

  def analyze(sc: SparkContext) {
    val records = sc.textFile("src/main/resources/sp500.csv").filter { x => !isHeader(x) }
    println("data loaded")
    val bars = records.map { x =>
      val elements = x.split(",")
      StockBar(elements(0),
        elements(1).toDouble,
        elements(2).toDouble,
        elements(3).toDouble,
        elements(4).toDouble,
        elements(5).toLong,
        elements(6).toDouble)
    }.sortBy(_.date, true)
    println("data mapped to bars")

    val windowSize = 250

    val pctChanges = bars.sliding(windowSize).map { xs =>
      val lastClose = xs.takeRight(1)(0).close
      val firstClose = xs.head.close

      100 * ((lastClose - firstClose) / firstClose)
    }
    //pctChanges.foreach(println)

    println("summary stats")
    println(pctChanges.stats())

    val binSize = 10
    val fractionPositive = pctChanges.filter(_ >= 0).count().toDouble / pctChanges.count().toDouble
    println("fraction positive: " + fractionPositive)

    val fractionPosOrSlightlyNeg = pctChanges.filter(_ >= (binSize * -1)).count().toDouble / pctChanges.count().toDouble
    println("fraction positive or slightly negative: " + fractionPosOrSlightlyNeg)

    val bins = pctChanges.map { x =>
      val posOrNeg = x.signum
      if (posOrNeg == -1) {
        ((x.toInt - binSize) / binSize) * binSize
      } else {
        (x.toInt / binSize) * binSize
      }
    }.countByValue().toList.sortBy(_._1)
    println("bins: " + bins)
    bins.foreach(println)

  }

  def isHeader(line: String): Boolean = line.startsWith("Date")

}

object StockIndexThingy {
  def main(args: Array[String]) {
    val master = "local[2]"
    val sparkHome = "/usr/local/spark-2.0.0-bin-hadoop2.7"
    val jars = Array[String]("/Users/dwadeson/trading-ecosystem/spark-data-analysis/target/scala-2.11/spark-data-analysis-assembly-0.2.0.jar")

    def appName = "Spark Test"

    val conf = new SparkConf()
      .setMaster(master)
      .setAppName(appName)
      .setSparkHome(sparkHome)
      .setJars(jars)

    val sc = SparkContext.getOrCreate(conf)

    new StockIndexThingy().analyze(sc)
  }
}