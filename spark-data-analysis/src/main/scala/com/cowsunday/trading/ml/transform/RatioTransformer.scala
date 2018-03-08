package com.cowsunday.trading.ml.transform

import org.apache.spark.ml.Transformer
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.ml.util.Identifiable
import org.apache.spark.sql.{DataFrame, Dataset}
import org.apache.spark.sql.types.{DataTypes, StructField, StructType}
import org.apache.spark.sql.functions._

class RatioTransformer(override val uid: String,
                       val numeratorCol: String,
                       val denominatorCol: String) extends Transformer {

  def this(numeratorCol: String, denominatorCol: String) = this(Identifiable.randomUID("myRt"), numeratorCol, denominatorCol)

  def copy(extra: ParamMap): RatioTransformer = {
    defaultCopy(extra)
  }

  def fullOutputColName(): String = s"$numeratorCol-$denominatorCol-ratio"

  override def transformSchema(schema: StructType): StructType = {
    schema.add(StructField(fullOutputColName(), DataTypes.DoubleType, false))
  }

  override def transform(df: Dataset[_]): DataFrame = {
    df.withColumn(fullOutputColName(), col(numeratorCol) / col(denominatorCol))
  }
}
