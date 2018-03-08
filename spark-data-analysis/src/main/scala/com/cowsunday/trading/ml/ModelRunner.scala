package com.cowsunday.trading.ml

import com.cowsunday.trading.ml.model.BinaryClassifier
import org.apache.spark.{SparkConf, SparkContext}

object ModelRunner {
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

    new BinaryClassifier().run(sc, "/users/dwadeson/trading-ecosystem/spark-data-analysis/src/main/resources/classification_data.txt")
  }
}
