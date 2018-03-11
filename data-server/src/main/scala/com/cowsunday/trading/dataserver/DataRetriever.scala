package com.cowsunday.trading.dataserver

import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source

case class DataPoint(x: Double, y: Double)
case class DataPoints(xName: String, yName: String, dataPoints: Seq[DataPoint])

object DataRetriever {

  val latestResultsFilePath = "../analysis-results/latestResults/latestResults.csv"
  val delimiter = ","

  // (fake) async data grab
  def fetchLatestData()(implicit ec: ExecutionContext): Future[Option[DataPoints]] = Future {

    val headers: Seq[String] = Source.fromFile(latestResultsFilePath).getLines.take(1).flatMap(line => {
      line.split(delimiter)
    }).toSeq
    // in-memory sequence - hope we dont get too much data... :)
    val data: Seq[DataPoint] = Source.fromFile(latestResultsFilePath).getLines.drop(1).map(line => {
      val values = line.split(delimiter)

      DataPoint(values(0).toDouble, values(1).toDouble)
    }).toSeq

    Option(DataPoints(headers(0), headers(1), data))
  }

}
