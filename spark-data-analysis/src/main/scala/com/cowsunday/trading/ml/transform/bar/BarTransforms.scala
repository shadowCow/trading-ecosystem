package com.cowsunday.trading.ml.transform.bar

import com.cowsunday.trading.ml.data._
import org.apache.spark.ml.param.{Param, ParamMap}
import org.apache.spark.ml.{Transformer, UnaryTransformer}
import org.apache.spark.ml.util.Identifiable
import org.apache.spark.sql.{Column, DataFrame, Dataset}
import org.apache.spark.sql.types.{DataType, DataTypes, StructField, StructType}
import org.apache.spark.sql.functions._

class BarTransforms {
  type BarTransform = PriceBar => Double

  val close: BarTransform = bar => bar.close
  val closeHighRange: BarTransform = bar => bar.high - bar.close
  val closeLowRange: BarTransform = bar => bar.close - bar.low
  val high: BarTransform = bar => bar.high
  val hiLowRange: BarTransform = bar => bar.high - bar.low
  val low: BarTransform = bar => bar.low
  val open: BarTransform = bar => bar.open
  val openCloseChange: BarTransform = bar => bar.getOpenCloseChange
  val openCloseRange: BarTransform = bar => bar.getOpenCloseRange
  val openHighRange: BarTransform = bar => bar.high - bar.open
  val openLowRange: BarTransform = bar => bar.open - bar.low

  val all: List[BarTransform] = List(
      close,
      closeHighRange,
      closeLowRange,
      high,
      hiLowRange,
      low,
      open,
      openCloseChange,
      openCloseRange,
      openHighRange,
      openLowRange
      )
}

class ArbitraryColOpTransformer(override val uid: String, val col: Column, val outputName: String) extends Transformer {

  def this(col: Column, outputName: String) = this(Identifiable.randomUID("arbCol"), col, outputName)

  def copy(extra: ParamMap): ArbitraryColOpTransformer = {
    new ArbitraryColOpTransformer(uid, col, outputName)
  }

  override def transformSchema(schema: StructType) = {
    schema.add(StructField(outputName, DataTypes.DoubleType, false))
  }

  override def transform(df: Dataset[_]): DataFrame = {
    df.withColumn(outputName, col)
  }
}

class BarTransformer(override val uid: String,
                     val fxn: ((Column, Column) => Column),
                     val inputColOne: String,
                     val inputColTwo: String,
                     val outputCol: String) extends Transformer {

  def this(fxn: ((Column, Column) => Column),
           inputColOne: String,
           inputColTwo: String,
           outputCol: String) = this(Identifiable.randomUID("myT"), fxn, inputColOne, inputColTwo, outputCol)

  def copy(extra: ParamMap): BarTransformer = {
    new BarTransformer(uid, fxn, inputColOne, inputColTwo, outputCol)
  }

  override def transformSchema(schema: StructType): StructType = {

    val checker = (colName: String) => {
      val idx1 = schema.fieldIndex(colName)
      val field1 = schema.fields(idx1)
      if (field1.dataType != DataTypes.DoubleType) {
        throw new Exception(s"Input Type ${field1.dataType} did not match input type DoubleType")
      }
    }
    checker(inputColOne)
    checker(inputColTwo)

    schema.add(StructField(outputCol, DataTypes.DoubleType, false))
  }

  override def transform(df: Dataset[_]): DataFrame = {
    val expr = fxn(df.col(inputColOne), df.col(inputColTwo))
    df.select(df.col("*"), expr.as(outputCol))
  }

}

class ChangeTransformer(override val inputColOne: String,
                        override val inputColTwo: String)
  extends BarTransformer((c1, c2) => c2 - c1,
    inputColOne,
    inputColTwo,
    s"$inputColOne-$inputColTwo-change")

class RangeTransformer(override val inputColOne: String,
                       override val inputColTwo: String)
  extends BarTransformer((c1, c2) => abs(c1 - c2),
    inputColOne,
    inputColTwo,
    s"$inputColOne-$inputColTwo-range")

case class OpenCloseRangeTransformer() extends RangeTransformer("o","c")
case class HighLowRangeTransformer() extends RangeTransformer("h","l")

class IsMultipleTransformer(override val inputColOne: String,
                            override val inputColTwo: String,
                            val multiple: Double,
                            val outputColOption: Option[String] = None)
  extends BarTransformer((c1, c2) => when(((c1/c2) >= multiple).or(c2 === lit(0.0)), 1.0).otherwise(0.0),
    inputColOne,
    inputColTwo,
    outputColOption.getOrElse(s"$inputColOne-$inputColTwo-isMult-$multiple"))

class IsLessThanMultipleTransformer(override val inputColOne: String,
                                    override val inputColTwo: String,
                                    val multiple: Double,
                                    val outputColOption: Option[String] = None)
  extends BarTransformer((c1, c2) => when(((c1/c2) >= multiple).or(c2 === lit(0.0)), 0.0).otherwise(1.0),
    inputColOne,
    inputColTwo,
    outputColOption.getOrElse(s"$inputColOne-$inputColTwo-isLessThanMult-$multiple"))

class IsEqualTransformer(override val inputColOne: String,
                         override val inputColTwo: String)
  extends BarTransformer((c1, c2) => when(c1 === c2, 1.0).otherwise(0.0),
    inputColOne,
    inputColTwo,
    s"$inputColOne-$inputColTwo-isEqual")

/*
Need some Open-Close Range to Open Low Range Multiple things.

And whatever other single bar performance normalizers we can think of.
 */