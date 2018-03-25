package com.cowsunday.trading.ml.transform

import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.{Column, DataFrame}
import org.apache.spark.sql.functions._

object ColumnProducers {

  // need to move this out somewhere as a parameter based on the dataset...
  val tickSize = 0.05

  import com.cowsunday.trading.ml.data.PriceDataSchemas.v1._
  import VariableTypes._

  val hlRange = NamedTransform(
    s"$high-$low-range",
    Continuous,
    (df) => {
      df.col(high) - df.col(low)
    })

  val ocRange = NamedTransform(
    s"$open-$close-range",
    Continuous,
    (df) => {
      abs(df.col(open) - df.col(close))
    })

  val ohRange = NamedTransform(
    s"$open-$high-range",
    Continuous,
    (df) => {
      df.col(high) - df.col(open)
    }
  )

  val olRange = NamedTransform(
    s"$open-$low-range",
    Continuous,
    (df) => {
      df.col(open) - df.col(low)
    }
  )

  val chRange = NamedTransform(
    s"$close-$high-range",
    Continuous,
    (df) => {
      df.col(high) - df.col(close)
    }
  )

  val clRange = NamedTransform(
    s"$close-$low-range",
    Continuous,
    (df) => {
      df.col(close) - df.col(low)
    }
  )

  val ocChange = NamedTransform(
    s"$open-$close-change",
    Continuous,
    (df) => {
      df.col(close) - df.col(open)
    }
  )

  val ocolRangeRatio = NamedTransform(
    s"$open-$close-$open-$low-rangeRatio",
    Continuous,
    (df) => {
      val ol = olRange.transform(df)
      // dont want to divide by zero.
      // our main interest is in stop size, so min value should be a single tick.
      val adjusted = when(ol === lit(0.0), tickSize).otherwise(ol)
      ocRange.transform(df) / adjusted
    }
  )

  val ocohRangeRatio = NamedTransform(
    s"$open-$close-$open-$high-rangeRatio",
    Continuous,
    (df) => {
      val oh = ohRange.transform(df)
      // dont want to divide by zero.
      // our main interest is in stop size, so min value should be a single tick.
      val adjusted = when(oh === lit(0.0), tickSize).otherwise(oh)
      ocRange.transform(df) / adjusted
    }
  )

  val upBodyOpeningGap = NamedTransform(
    "upBodyOpeningGap",
    Binary,
    (df) => {
      val window = Window.orderBy(date)

      val laggedOpen = lag(df.col(open),1,0).over(window)
      val laggedClose = lag(df.col(close),1,0).over(window)

      when(df.col(open) > laggedOpen && df.col(open) > laggedClose, 1.0).otherwise(0.0)
    }
  )

  val downBodyOpeningGap = NamedTransform(
    "downBodyOpeningGap",
    Binary,
    (df) => {
      val window = Window.orderBy(date)

      val laggedOpen = lag(df.col(open),1,0).over(window)
      val laggedClose = lag(df.col(close),1,0).over(window)

      when(df.col(open) < laggedOpen && df.col(open) < laggedClose, 1.0).otherwise(0.0)
    }
  )

  val upWickOpeningGap = NamedTransform(
    "upWickOpeningGap",
    Binary,
    (df) => {
      val window = Window.orderBy(date)

      val laggedHigh = lag(df.col(high),1,0).over(window)

      when(df.col(open) > laggedHigh, 1.0).otherwise(0.0)
    }
  )

  val downWickOpeningGap = NamedTransform(
    "downWickOpeningGap",
    Binary,
    (df) => {
      val window = Window.orderBy(date)

      val laggedLow = lag(df.col(low),1,0).over(window)

      when(df.col(open) < laggedLow, 1.0).otherwise(0.0)
    }
  )

  val transforms: Seq[NamedTransform] = Seq(
    hlRange,
    ocRange,
    ohRange,
    olRange,
    chRange,
    clRange,
    ocChange,
    ocolRangeRatio,
    ocohRangeRatio,
    upBodyOpeningGap,
    upWickOpeningGap,
    downBodyOpeningGap,
    downWickOpeningGap
  )

}

object VariableTypes extends Enumeration {
  val Binary, Multiclass, Continuous = Value
}

case class NamedTransform(name: String,
                          outputType: VariableTypes.Value,
                          transform: DataFrame => Column)