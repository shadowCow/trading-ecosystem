package com.cowsunday.trading.ml.transform

import org.apache.spark.sql.{Column, DataFrame}
import org.apache.spark.sql.functions._

object ColumnProducers {

  import com.cowsunday.trading.ml.data.PriceDataSchemas.v1._

  val hlRange: NamedTransform = NamedTransform(
    s"$high-$low range",
    (df) => {
      df.col(high) - df.col(low)
    })

  val ocRange: NamedTransform = NamedTransform(
    s"$open-$close range",
    (df) => {
      abs(df.col(open) - df.col(close))
    })

  val transforms: Seq[NamedTransform] = Seq(
    hlRange,
    ocRange
  )

}

case class NamedTransform(name: String, transform: DataFrame => Column)