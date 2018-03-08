package com.cowsunday.trading.ml.transform.sliding

import com.cowsunday.trading.ml.data._
import org.apache.spark.ml.Transformer
import org.apache.spark.ml.param.ParamMap
import org.apache.spark.ml.util.Identifiable
import org.apache.spark.sql.{Column, DataFrame, Dataset, Row}
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.types.{DataTypes, StructField, StructType}
import org.apache.spark.sql.functions._
import org.apache.spark.mllib.rdd.RDDFunctions._

class SlidingTransforms {
  /**
   * Any sliding transforms that require multiple points of data from the PriceBars to compute.
   */
  type SlidingPriceBarTransform = Array[PriceBar] => Double

  val hiLowRange: SlidingPriceBarTransform = window => window.maxBy(_.high).high - window.minBy(_.low).low
  val openCloseRange: SlidingPriceBarTransform = window => window.maxBy(_.close).close - window.minBy(_.open).open

  val allPriceBar = List(
      hiLowRange,
      openCloseRange
      )

  type SlidingDoubleTransform = Array[Double] => Double

  val absoluteDifference: SlidingDoubleTransform = window => math.abs(window.last - window.head)
  val difference: SlidingDoubleTransform = window => window.last - window.head
  val max: SlidingDoubleTransform = window => window.max
  val min: SlidingDoubleTransform = window => window.min
  val range: SlidingDoubleTransform = window => window.max - window.min
  val average: SlidingDoubleTransform = window => window.sum / window.length

  val allDouble = List(
      absoluteDifference,
      difference,
      max,
      min,
      range,
      average
      )

  /**
   * Binary sliding transforms
   */
  type SlidingBinaryTransform = Array[Double] => BinaryVariable

  val isUpStreak: SlidingBinaryTransform = window => {
    val lagged = window.drop(1)
    val original = window.dropRight(1)
    BinaryVariable(original.zip(lagged).forall({ case (o,l) => o < l}))
  }
  val isDownStreak: SlidingBinaryTransform = window => {
    val lagged = window.drop(1)
    val original = window.dropRight(1)
    BinaryVariable(original.zip(lagged).forall({ case (o,l) => o > l}))
  }

  /**
   * Comparison of last value to window
   */
  type SlidingComparisonTransform = (Array[Double], Double) => BinaryVariable

  val isMax: SlidingComparisonTransform = (window, last) => BinaryVariable(window.max == last)
  val isMin: SlidingComparisonTransform = (window, last) => BinaryVariable(window.min == last)
}

/*
i guess we need some generic fxn on columns?
with generic columns list input...?

that would be handy if we just defined the transform functions in one place so that we could reuse them across 'transformers'
 */
class WindowTransformer(override val uid: String,
                        val outputCol: String,
                        val windowLength: Int,
                        val columnOp: Column) extends Transformer {

  def this(outputCol: String, windowLength: Int, columnOp: Column) = this(Identifiable.randomUID("myW"), outputCol, windowLength, columnOp)

  def copy(extra: ParamMap): WindowTransformer = {
    new WindowTransformer(uid, outputCol, windowLength, columnOp)
  }

  def fullOutputColName(): String = s"$outputCol-$windowLength"

  override def transformSchema(schema: StructType): StructType = {
    schema.add(StructField(fullOutputColName(), DataTypes.DoubleType, false))
  }

  override def transform(df: Dataset[_]): DataFrame = {
    val mywindow = Window.rowsBetween(-(windowLength-1),0)

    df.withColumn(fullOutputColName(), columnOp.over(mywindow))
  }
}

class WindowAverageTransformer(val inputCol: String, override val windowLength: Int)
  extends WindowTransformer(s"avg-$inputCol", windowLength, avg(inputCol))

class WindowRangeTransformer(val inputColOne: String, val inputColTwo: String, override val windowLength: Int)
  extends WindowTransformer(s"range-$inputColOne-$inputColTwo", windowLength, max(inputColOne) - min(inputColTwo))

class HiLoWindowRangeTransformer(override val windowLength: Int)
  extends WindowRangeTransformer("High", "Low", windowLength)
class CloseOpenWindowRangeTransformer(override val windowLength: Int)
  extends WindowRangeTransformer("Close", "Open", windowLength)

class WindowChangeTransformer(val inputColOne: String, val inputColTwo: String, override val windowLength: Int)
  extends WindowTransformer(s"change-$inputColOne-$inputColTwo", windowLength, last(inputColTwo) - first(inputColOne))

class OpenCloseWindowChangeTransformer(override val windowLength: Int)
  extends WindowChangeTransformer("Open", "Close", windowLength)

class MaxWindowTransformer(val inputCol: String, override val windowLength: Int)
  extends WindowTransformer(s"max-$inputCol", windowLength, max(inputCol))
class MinWindowTransformer(val inputCol: String, override val windowLength: Int)
  extends WindowTransformer(s"min-$inputCol", windowLength, min(inputCol))

class LeadTransformer(override val uid: String, val inputCol: String, val leadValue: Int) extends Transformer {
  def this(inputCol: String, leadValue: Int) = this(Identifiable.randomUID("myL"), inputCol, leadValue)

  def copy(extra: ParamMap): LeadTransformer = {
    new LeadTransformer(uid, inputCol, leadValue)
  }

  def fullOutputColName(): String = s"lead-$inputCol-$leadValue"

  override def transformSchema(schema: StructType): StructType = {
    val idx = schema.fieldIndex(inputCol)
    val fieldType = schema.fields(idx).dataType

    schema.add(StructField(fullOutputColName(), fieldType, false))
  }

  override def transform(df: Dataset[_]): DataFrame = {
    val window = Window.orderBy("trading_date")

    df.withColumn(fullOutputColName(), lead(inputCol, leadValue, 0).over(window))
  }
}

class LagTransformer(override val uid: String, val inputCol: String, val lagValue: Int) extends Transformer {
  def this(inputCol: String, lagValue: Int) = this(Identifiable.randomUID("myL"), inputCol, lagValue)

  def copy(extra: ParamMap): LeadTransformer = {
    new LeadTransformer(uid, inputCol, lagValue)
  }

  def fullOutputColName(): String = s"lead-$inputCol-$lagValue"

  override def transformSchema(schema: StructType): StructType = {
    val idx = schema.fieldIndex(inputCol)
    val fieldType = schema.fields(idx).dataType

    schema.add(StructField(fullOutputColName(), fieldType, false))
  }

  override def transform(df: Dataset[_]): DataFrame = {
    val window = Window.orderBy("trading_date")

    df.withColumn(fullOutputColName(), lag(inputCol, lagValue, 0).over(window))
  }
}

class IsStreakTransformer(override val uid: String, val inputCol: String, val windowLength: Int, val isUp: Boolean) extends Transformer {
  def this(inputCol: String, windowLength: Int, isUp: Boolean) = this(Identifiable.randomUID("myStr"), inputCol, windowLength, isUp)

  def copy(extra: ParamMap): IsStreakTransformer = {
    new IsStreakTransformer(uid, inputCol, windowLength, isUp)
  }

  def fullOutputColName(): String = s"${if (isUp) "up" else "down"}Str-$inputCol"

  override def transformSchema(schema: StructType): StructType = {
    schema.add(StructField(fullOutputColName(), DataTypes.DoubleType, false))
  }

  override def transform(dataset: Dataset[_]) = {
    val window = Window.orderBy("trading_date")

    val laggedCol = dataset.withColumn("tmp-lag", lag(inputCol, 1, 0).over(window))
    val mywindow = Window.rowsBetween(-(windowLength-1),0)

    if (isUp) {
      val df = laggedCol.withColumn("tmp-count", sum(when(col("tmp-lag") < col(inputCol), 1.0).otherwise(0.0)).over(mywindow))
      df.withColumn(fullOutputColName(), when(col("tmp-count") === windowLength, 1.0).otherwise(0.0))
    } else {
      val df = laggedCol.withColumn("tmp-count", sum(when(col("tmp-lag") > col(inputCol), 1.0).otherwise(0.0)).over(mywindow))
      df.withColumn(fullOutputColName(), when(col("tmp-count") === windowLength, 1.0).otherwise(0.0))
    }
  }
}

class StreakCounterTransformer(override val uid: String, val inputCol: String, val windowLength: Int, val isUp: Boolean) extends Transformer {
  def this(inputCol: String, windowLength: Int, isUp: Boolean) = this(Identifiable.randomUID("myStr"), inputCol, windowLength, isUp)

  def copy(extra: ParamMap): StreakCounterTransformer = {
    new StreakCounterTransformer(uid, inputCol, windowLength, isUp)
  }

  def fullOutputColName(): String = s"${if (isUp) "up" else "down"}StrCnt-$inputCol"

  override def transformSchema(schema: StructType): StructType = {
    schema.add(StructField(fullOutputColName(), DataTypes.DoubleType, false))
  }

  override def transform(dataset: Dataset[_]) = {
    import dataset.sparkSession.implicits._

    val window = Window.orderBy("trading_date")

    val laggedCol = dataset.withColumn("tmp-lag", lag(inputCol, 1, 0).over(window))
    val mywindow = Window.rowsBetween(-(windowLength-1),0)

    if (isUp) {
      val withUpCol = laggedCol.withColumn("isUp", when(col("tmp-lag") < col(inputCol), 1.0).otherwise(0.0))

      val lastZeroRowDate = max(when(col("isUp") === 0.0, col("trading_date")).otherwise(0)).over(mywindow)
      val withLastZeroRow = withUpCol.withColumn("lastZeroDate", lastZeroRowDate)

      val sumSince = withLastZeroRow.rdd.sliding(windowLength).map(w => {
        val lastZeroDate = w.last.getAs[Int]("lastZeroDate")
        val theSum = w.filter(r => r.getAs[Int]("trading_date") > lastZeroDate).map(r => r.getAs[Double]("isUp")).sum
        (w.last.getAs[Int]("trading_date"),theSum)
      }).toDF("td",fullOutputColName())

      withUpCol.join(sumSince, dataset("trading_date") === sumSince("td"), "left").drop("td").drop("tmp-lag").drop("isUp").orderBy(asc("trading_date"))

    } else {
      val withDownCol = laggedCol.withColumn("isDown", when(col("tmp-lag") > col(inputCol), 1.0).otherwise(0.0))

      val lastZeroRowDate = max(when(col("isDown") === 0.0, col("trading_date")).otherwise(0)).over(mywindow)
      val withLastZeroRow = withDownCol.withColumn("lastZeroDate", lastZeroRowDate)

      val sumSince = withLastZeroRow.rdd.sliding(windowLength).map(w => {
        val lastZeroDate = w.last.getAs[Int]("lastZeroDate")
        val theSum = w.filter(r => r.getAs[Int]("trading_date") > lastZeroDate).map(r => r.getAs[Double]("isDown")).sum
        (w.last.getAs[Int]("trading_date"),theSum)
      }).toDF("td",fullOutputColName())

      withDownCol.join(sumSince, dataset("trading_date") === sumSince("td"), "left").drop("td").drop("tmp-lag").drop("isDown").orderBy(asc("trading_date"))
    }
  }
}

case class HigherLowStreakCounter(override val windowLength: Int) extends StreakCounterTransformer("l",windowLength,true)
case class HigherCloseStreakCounter(override val windowLength: Int) extends StreakCounterTransformer("c",windowLength,true)
case class HigherOpenStreakCounter(override val windowLength: Int) extends StreakCounterTransformer("o",windowLength,true)
case class HigherHighStreakCounter(override val windowLength: Int) extends StreakCounterTransformer("h",windowLength,true)

case class LowerLowStreakCounter(override val windowLength: Int) extends StreakCounterTransformer("l",windowLength,false)
case class LowerCloseStreakCounter(override val windowLength: Int) extends StreakCounterTransformer("c",windowLength,false)
case class LowerOpenStreakCounter(override val windowLength: Int) extends StreakCounterTransformer("o",windowLength,false)
case class LowerHighStreakCounter(override val windowLength: Int) extends StreakCounterTransformer("h",windowLength,false)

/*
Opening Gap
 */
sealed trait GapType
case object BodyGap extends GapType
case object WickGap extends GapType

class OpeningGap(override val uid: String, val isUp: Boolean, gapType: GapType) extends Transformer {
  def this(isUp: Boolean, gapType: GapType) = this(Identifiable.randomUID("openingGap"), isUp, gapType)

  def copy(extra: ParamMap): OpeningGap = {
    new OpeningGap(uid, isUp, gapType)
  }

  def fullOutputColName(): String = s"${if (isUp) "up" else "down"}OpenGap-${gapType}"

  override def transformSchema(schema: StructType): StructType = {
    schema.add(StructField(fullOutputColName(), DataTypes.DoubleType, false))
  }

  override def transform(dataset: Dataset[_]) = {
    val window = Window.orderBy("trading_date")

    val laggedOpen = dataset.withColumn("lagOpen", lag("o",1,0).over(window))
    val laggedClose = laggedOpen.withColumn("lagClose", lag("c",1,0).over(window))
    val laggedHigh = laggedClose.withColumn("lagHigh", lag("h",1,0).over(window))
    val laggedLow = laggedHigh.withColumn("lagLow", lag("l",1,0).over(window))

    val df = gapType match {
      case BodyGap if isUp =>
        laggedLow.withColumn(fullOutputColName(), when(col("o") > col("lagOpen") && col("o") > col("lagClose"), 1.0).otherwise(0.0))
      case BodyGap =>
        laggedLow.withColumn(fullOutputColName(), when(col("o") < col("lagOpen") && col("o") < col("lagClose"), 1.0).otherwise(0.0))
      case WickGap if isUp =>
        laggedLow.withColumn(fullOutputColName(), when(col("o") > col("lagHigh"), 1.0).otherwise(0.0))
      case WickGap =>
        laggedLow.withColumn(fullOutputColName(), when(col("o") < col("lagLow"), 1.0).otherwise(0.0))
    }
    df.drop("lagOpen").drop("lagClose").drop("lagHigh").drop("lagLow")

  }
}

case class UpOpeningGapBody() extends OpeningGap(true, BodyGap)
case class DownOpeningGapBody() extends OpeningGap(false, BodyGap)
case class UpOpeningGapWick() extends OpeningGap(true, WickGap)
case class DownOpeningGapWick() extends OpeningGap(false, WickGap)

/*
Streak reversal...
 */

/*
Need some performance normalizers based on average volatility or something.
 */