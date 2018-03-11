package com.cowsunday.trading.dataserver

import scala.concurrent.Future

case class DataPoint(x: Int, y: Int)
case class DataPoints(dataPoints: Seq[DataPoint])

object DataRetriever {

  // (fake) async data grab
  def fetchData(): Future[Option[DataPoints]] = Future {

    val dummyData = (0 until 30).map(i => {
      val x = math.random * 100
      val y = math.random * 100

      DataPoint(x.toInt,y.toInt)
    })

    Option(DataPoints(dummyData))
  }

}
