package com.cowsunday.trading.ml.join

import com.cowsunday.trading.ml.SparkSpec

class JoinDataFramesSpec extends SparkSpec {

  feature("I can join data frames together") {
    scenario("join data sets by date") {

      def marketPrefixedNames(symbol: String): Seq[String] = {
        Seq("trading_date") ++ Seq("o", "h", "l", "c").map(c => s"$symbol.$c")
      }

      val spData = sparkSession.read
        .option("header", "true")
        .option("inferSchema", "true")
        .option("sep"," ")
        .csv("src/main/resources/sp103_daily.csv")
        .drop("trading_time")
        .drop("atr")
        .drop("volume")
        .toDF(marketPrefixedNames("sp"): _*)

      val bondData = sparkSession.read
        .option("header", "true")
        .option("inferSchema", "true")
        .option("sep"," ")
        .csv("src/main/resources/us103_daily.csv")
        .drop("trading_time")
        .drop("atr")
        .drop("volume")
        .toDF(marketPrefixedNames("us"): _*)


      val joined = spData.join(bondData, "trading_date")
      joined.show()
    }
  }
}
