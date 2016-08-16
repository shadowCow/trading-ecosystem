package com.cowsunday.trading.ml

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.specs2._
import org.specs2.mutable.{ Before }
import runner.SpecificationsFinder

trait SparkBeforeAfter extends Before {

  private val master = "local[2]"
  private val sparkHome = "/usr/local/spark-2.0.0-bin-hadoop2.7"
  private val jars = Array[String]("/Users/dwadeson/trading-ecosystem/spark-data-analysis/target/scala-2.11/spark-data-analysis-assembly-0.2.0.jar")

  var sc: SparkContext = _

  def appName = "Spark Test"

  override def before = {
    val conf = new SparkConf()
                  .setMaster(master)
                  .setAppName(appName)
                  .setSparkHome(sparkHome)
                  .setJars(jars)

    sc = SparkContext.getOrCreate(conf)
  }

}