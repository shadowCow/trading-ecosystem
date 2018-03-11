package com.cowsunday.trading.ml.ondemand

import java.io.{File, FilenameFilter}

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{Column, DataFrame, SaveMode, SparkSession}

import scala.util.Try

object OnDemandAnalysis {

  import com.cowsunday.trading.ml.transform.ColumnProducers._

  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.ERROR)
    Logger.getLogger("akka").setLevel(Level.ERROR)

    implicit val sparkSession = SparkSession.builder()
      .master("local")
      .appName("Trading Algo Pipeline")
      .getOrCreate()

    val result = ResultsGenerator.transformAndSave(
      "src/main/resources/sp103_daily.csv",
      hlRange,
      ocRange
    )

    sparkSession.stop()
  }
}
