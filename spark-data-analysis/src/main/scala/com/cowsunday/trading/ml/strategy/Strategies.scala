package com.cowsunday.trading.ml.strategy

import org.apache.spark.sql.{Column, DataFrame}
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._

object Strategies {

  import com.cowsunday.trading.ml.data.PriceDataSchemas.v1._

  val openToOpenChange = lead(col(open), 1, 0).over(Window.orderBy(date)) - col(open)

  val openToOpenLong = Strategy(
    "openToOpenLong",
    openToOpenChange
  )

  val openToOpenShort = Strategy(
    "openToOpenShort",
    lit(-1.0) * openToOpenChange
  )


  def evaluate(df: DataFrame, strategy: Strategy): Double = {
    val withSum = df.agg(
      sum(
        when(col(StrategySchemas.v1.tradingDecision) === 1.0, col(strategy.name)).otherwise(0.0)
      )
    )

    withSum.first().getDouble(0)
  }
}

case class Strategy(name: String,
                    column: Column)


object StrategySchemas {

  val v1 = StrategySchema("trading_decision", "strategy_results")

}

case class StrategySchema(tradingDecision: String,
                          results: String)