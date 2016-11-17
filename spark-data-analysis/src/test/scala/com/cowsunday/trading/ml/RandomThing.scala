package com.cowsunday.trading.ml

import org.apache.spark.sql.SparkSession
import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.mutable._
import org.specs2.runner.JUnitRunner
import org.specs2.matcher.{ Expectable, Matcher }
import com.cowsunday.trading.ml.data.PriceBar
import com.cowsunday.trading.ml.transform.bar._
import com.cowsunday.trading.ml.transform.sliding._

@RunWith(classOf[JUnitRunner])
class RandomThing extends Specification with SparkBeforeAfter {

  "SparkSql" should {
    "work" in {
      val session = SparkSession
        .builder()
        .appName("sql stuffs")
        .getOrCreate()

      import session.implicits._
      val df = session.read.json("src/test/resources/test_pricedata/mydata.json").as[PriceBar]

      val df2 = session.read.json("src/test/resources/test_pricedata/mydata2.json").as[PriceBar]

      val combined = df.join(df2, "date")

      df.createOrReplaceTempView("market1")
      df2.createOrReplaceTempView("market2")

      val open1 = session.sql("SELECT open from market1 where date = 20160102")
      val open2 = session.sql("SELECT open from market2 where date = 20160102")

      open1.show()
      open2.show()

      println("opens: " + open1.take(1)(0) + ", " + open2.take(2)(0))

      1 mustEqual 1
    }
  }
}