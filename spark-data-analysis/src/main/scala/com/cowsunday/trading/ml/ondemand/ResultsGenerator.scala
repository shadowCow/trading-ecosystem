package com.cowsunday.trading.ml.ondemand

import java.io.{File, FilenameFilter}

import com.cowsunday.trading.ml.transform.NamedTransform
import org.apache.log4j.Logger
import org.apache.spark.sql.{Column, DataFrame, SaveMode, SparkSession}

import scala.util.Try

object ResultsGenerator {

  val logger = Logger.getLogger(ResultsGenerator.getClass)

  def transformAndSave(sourceData: String,
                       featureTransform: NamedTransform,
                       metricTransform: NamedTransform)(implicit sparkSession: SparkSession): Boolean = {

    val results = generateResults(sourceData, featureTransform, metricTransform)

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

    val didRename = Try(
      new File(s"$outputDirectory/$myCsv").renameTo(new File(s"$outputDirectory/$csvFileName"))
    ).getOrElse(false)

    logger.info(s"${if (didRename) "[SUCCESS]" else "[FAILED]"} - Rename output to latestResults.csv")
    didRename
  }

  def generateResults(sourceData: String,
                      featureTransform: NamedTransform,
                      metricTransform: NamedTransform)(implicit sparkSession: SparkSession): DataFrame = {

    val df = sparkSession.read
      .option("header", "true")
      .option("inferSchema", "true")
      .option("sep"," ")
      .csv(sourceData)
      .drop("trading_time")
      .drop("atr")
      .drop("volume")

    df.select(featureTransform.transform(df).as("feature"), metricTransform.transform(df).as("metric"))
  }
}
