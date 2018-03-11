package com.cowsunday.trading.ml.ondemand

import java.io.{File, FilenameFilter}

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{SaveMode, SparkSession}

import scala.util.Try

object OnDemandAnalysis {

  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.ERROR)
    Logger.getLogger("akka").setLevel(Level.ERROR)

    val sparkSession = SparkSession.builder()
      .master("local")
      .appName("Trading Algo Pipeline")
      .getOrCreate()

    val results = ResultsGenerator.generateResults("src/main/resources/sp103_daily.csv", sparkSession)

    val outputDirectory = "../analysis-results/latestResults"
    results.coalesce(1).write
      .option("sep",",")
      .option("header","true")
      .mode(SaveMode.Overwrite)
      .csv(outputDirectory)

    val myCsv = new File(outputDirectory).list(new FilenameFilter {
      override def accept(dir: File, name: String) = name.endsWith(".csv")
    }).head

    val csvFileName = "latestResults.csv"
    println(s"Renaming $myCsv to $csvFileName...")
    val didRename = Try(
      new File(s"$outputDirectory/$myCsv").renameTo(new File(s"$outputDirectory/$csvFileName"))
    ).getOrElse(false)
    println(s"${if (didRename) "Successfully renamed" else "Failed to rename"}")


    sparkSession.stop()
  }
}
