package com.cowsunday.trading.ml

import com.cowsunday.trading.ml.data.PriceDataSchemas
import com.cowsunday.trading.ml.strategy.StrategySchemas
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.scalatest.{FeatureSpec, Matchers}

abstract class SparkSpec extends FeatureSpec with Matchers {

  // margin of error for checking equality for doubles
  val doubleErr = 0.0001

  implicit val sparkSession = SparkSession.builder()
    .master("local")
    .appName("Trading Algo Pipeline")
    .getOrCreate()

  import sparkSession.implicits._
  val priceDataSchema = PriceDataSchemas.v1
  import priceDataSchema._

  val strategySchema = StrategySchemas.v1

  def getSimpleData(): DataFrame = TestData.simple.toDF(date, open, high, low, close)
}

object TestData {


  val simple = Seq(
    (20170101, 1.5, 3.0, 1.0, 2.5),
    (20170102, 2.75, 4.1, 2.0, 3.5),
    (20170103, 4.25, 6.0, 4.25, 6.0),
    (20170104, 4.4, 4.6, 4.25, 4.4),
    (20170105, 3.4, 3.5, 1.9, 2.4)
  )

}
