package com.cowsunday.trading.ml.strategy

import com.cowsunday.trading.ml.strategy.Strategies
import com.cowsunday.trading.ml.{SparkSpec, TestData}
import org.apache.spark.sql.functions._

class TradingStrategySpec extends SparkSpec {

  import priceDataSchema._
  import strategySchema._

  feature("I can evaluate trading strategies") {

    scenario("I can evaluate open-to-open strategies") {

      val data = getSimpleData()

      val strat = Strategies.openToOpenLong

      val withStrat = data.withColumn(strat.name, strat.column)

      val withDecisions = withStrat.withColumn(
        tradingDecision,
        when(col(date) === 20170101 || col(date) === 20170103, 1.0).otherwise(0.0)
      )

      val performance = Strategies.evaluate(withDecisions, strat)

      performance shouldBe 1.4 +- doubleErr

      /*
      And going short...
       */
      val shortStrat = Strategies.openToOpenShort
      val withStratShort = data.withColumn(shortStrat.name, shortStrat.column)

      val withDecisionsShort = withStratShort.withColumn(
        tradingDecision,
        when(col(date) === 20170101 || col(date) === 20170103, 1.0).otherwise(0.0)
      )

      val performanceShort = Strategies.evaluate(withDecisionsShort, shortStrat)

      performanceShort shouldBe -1.4 +- doubleErr
    }
  }
}
