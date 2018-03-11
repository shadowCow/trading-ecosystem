package com.cowsunday.trading.ml.ondemand

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._

object ResultsGenerator {


  def generateResults(sourceData: String, sparkSession: SparkSession): DataFrame = {

    val df = sparkSession.read
      .option("header", "true")
      .option("inferSchema", "true")
      .option("sep"," ")
      .csv(sourceData)
      .drop("trading_time")
      .drop("atr")
      .drop("volume")

    df.select(col("o").as("open"), col("c").as("close"))
  }
}
