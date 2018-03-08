package com.cowsunday.trading.ml

import com.cowsunday.trading.ml.data.PriceBar
import com.cowsunday.trading.ml.transform.RatioTransformer
import com.cowsunday.trading.ml.transform.bar._
import com.cowsunday.trading.ml.transform.sliding._
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkContext
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.classification.{LinearSVC, LogisticRegression}
import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.linalg.{Vector, Vectors}
import org.apache.spark.ml.regression.DecisionTreeRegressor
import org.apache.spark.ml.tuning.{CrossValidator, ParamGridBuilder}
import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
import org.apache.spark.mllib.optimization.SquaredL2Updater
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.{Dataset, Row, SQLImplicits, SparkSession}
import org.apache.spark.sql.functions._

object TradingAlgoPipeline {

  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.ERROR)
    Logger.getLogger("akka").setLevel(Level.ERROR)

    val sparkSession = SparkSession.builder()
      .master("local")
      .appName("Trading Algo Pipeline")
      .getOrCreate()
    import sparkSession.implicits._

    val df = sparkSession.read
      .option("header", "true")
      .option("inferSchema", "true")
      .option("sep"," ")
      .csv("src/main/resources/sp103_daily.csv")
      .drop("trading_time")
      .drop("atr")
      .drop("volume")

    val numRows = df.count()
    println(numRows)

    val training = df.limit(5000)
    println("training")
    println(training.count())
    training.show()
    val test = df.sort(col("trading_date").desc).limit(numRows.toInt - 5000).sort(col("trading_date").asc)
    val testSize = test.count()
    println("test")
    println(test.count())
    test.show()

    val training2 = df.sort(col("trading_date").desc).limit(5000).sort(col("trading_date").asc)
    val test2 = df.limit(numRows.toInt - 5000)

    /*
    end transform experiments
     */
    import org.apache.spark.mllib.rdd.RDDFunctions._
    val t1 = training.withColumn("tmp-lag-l", lag("l",1,0).over(Window.orderBy("trading_date")))
    val t2 = t1.withColumn("isUp-l", when(col("l") > col("tmp-lag-l"), 1.0).otherwise(0.0))
    val t3 = t2.withColumn("tmp-lag-c", lag("c",1,0).over(Window.orderBy("trading_date")))
    val t4 = t3.withColumn("isUp-c", when(col("c") > col("tmp-lag-c"), 1.0).otherwise(0.0))
    val t5 = t4.withColumn("tmp-lag-h", lag("h",1,0).over(Window.orderBy("trading_date")))
    val t6 = t5.withColumn("isDown-h", when(col("h") < col("tmp-lag-h"), 1.0).otherwise(0.0))
    val t7 = t6.withColumn("isDown-c", when(col("c") < col("tmp-lag-c"), 1.0).otherwise(0.0))
    val t8 = t7.rdd.sliding(3).map(w => {
      val ind1 = w.lastIndexWhere(r => r.getAs[Double]("isUp-l") == 0.0)
      val s1 = if (ind1 < 0) {
        w.map(r => r.getAs[Double]("isUp-l")).sum
      } else {
        val td1 = w(ind1).getAs[Int]("trading_date")
        w.filter(r => r.getAs[Int]("trading_date") > td1).map(r => r.getAs[Double]("isUp-l")).sum
      }

      val ind2 = w.lastIndexWhere(r => r.getAs[Double]("isUp-c") == 0.0)
      val s2 = if (ind2 < 0) {
        w.map(r => r.getAs[Double]("isUp-c")).sum
      } else {
        val td2 = w(ind2).getAs[Int]("trading_date")
        w.filter(r => r.getAs[Int]("trading_date") > td2).map(r => r.getAs[Double]("isUp-c")).sum
      }

      val ind3 = w.lastIndexWhere(r => r.getAs[Double]("isDown-h") == 0.0)
      val s3 = if (ind3 < 0) {
        w.map(r => r.getAs[Double]("isDown-h")).sum
      } else {
        val td3 = w(ind3).getAs[Int]("trading_date")
        w.filter(r => r.getAs[Int]("trading_date") > td3).map(r => r.getAs[Double]("isDown-h")).sum
      }

      val ind4 = w.lastIndexWhere(r => r.getAs[Double]("isDown-c") == 0.0)
      val s4 = if (ind4 < 0) {
        w.map(r => r.getAs[Double]("isDown-c")).sum
      } else {
        val td4 = w(ind4).getAs[Int]("trading_date")
        w.filter(r => r.getAs[Int]("trading_date") > td4).map(r => r.getAs[Double]("isDown-c")).sum
      }

      (w.last.getAs[Int]("trading_date"),
        w.last.getAs[Double]("o"),
        w.last.getAs[Double]("h"),
        w.last.getAs[Double]("l"),
        w.last.getAs[Double]("c"),
        s1, s2, s3, s4)
    }).toDF("trading_date", "o","h","l","c","higher-low-str", "higher-close-str", "lower-high-str", "lower-close-str")


    val windowLength = 3
//    val higherLowStreakCounter = HigherLowStreakCounter(windowLength)
//    val higherCloseStreakCounter = HigherCloseStreakCounter(windowLength)
//    val lowerHighStreakCounter = LowerHighStreakCounter(windowLength)
//    val lowerCloseStreakCounter = LowerCloseStreakCounter(windowLength)
    val upOpeningGapBody = UpOpeningGapBody()
    val upOpeningGapWick = UpOpeningGapWick()
    val downOpeningGapBody = DownOpeningGapBody()
    val downOpeningGapWick = DownOpeningGapWick()

//    val d1 = higherLowStreakCounter.transform(training)
//    val d2 = higherCloseStreakCounter.transform(d1)
//    val d3 = lowerHighStreakCounter.transform(d2)
//    val d4 = lowerCloseStreakCounter.transform(d3)
    val d5 = upOpeningGapBody.transform(t8)
    val d6 = upOpeningGapWick.transform(d5)
    val d7 = downOpeningGapBody.transform(d6)
    val d8 = downOpeningGapWick.transform(d7)

    d8.show()

    /*
    Testing out a full example...
    We'll use a window of 3
    We'll have two predictors:
    (1) is highest close
    (2) is highest (close - open) range

    And our label is...
    (-) open,close change is at least 2x (open - low)
     */
//    val isUpStreak = new IsStreakTransformer("h", 3, true)
//    val withIsUp = isUpStreak.transform(df)
//    withIsUp.show()
//    val isDownStreak = new IsStreakTransformer("h", 3, false)
//    isDownStreak.transform(withIsUp).show()
//
//    val predOneTransformA = new MaxWindowTransformer("c", 3)
//    val predOneTransformB = new IsEqualTransformer("c", "max-c-3")
//    val predTwoTransformA = new RangeTransformer("c", "o")
//    val predTwoTransformB = new MaxWindowTransformer("c-o-range", 3)
//    val predTwoTransformC = new IsEqualTransformer("c-o-range", "max-c-o-range-3")
//
//    val labelTransformA = new RangeTransformer("o", "h")
//    val labelTransformA1 = new LeadTransformer("o-h-range", 1)
//    val labelTransformB = new RangeTransformer("o", "l")
//    val labelTransformB1 = new LeadTransformer("o-l-range", 1)
//    val labelTransformC = new RangeTransformer("o","c")
//    val labelTransformC1 = new WindowAverageTransformer("o-c-range", 3)
//    val lowRatio = 0.2
//    val labelTransformD = new ArbitraryColOpTransformer(col("lead-o-h-range-1") / (lit(lowRatio) * col("avg-o-c-range-3")), "rr-ratio")
//    val labelTransformD1 = new ArbitraryColOpTransformer(col("lead-o-l-range-1") / (lit(lowRatio) * col("avg-o-c-range-3")), "loss-rr-ratio")
//
//    val labelTransformE = new ArbitraryColOpTransformer(when((col("lead-o-l-range-1") < lit(lowRatio) * col("avg-o-c-range-3")) && (col("lead-o-h-range-1") > lit(1.6) * col("avg-o-c-range-3")), 1.0).otherwise(0.0), "label")

    //val labelTransformC = new IsMultipleTransformer("lead-o-c-change-1", "lead-o-l-range-1", 2, Some("label"))

//    val featuresAssembler = new VectorAssembler()
//      .setInputCols(Array("c-max-c-3-isEqual", "c-o-range-max-c-o-range-3-isEqual"))
//      .setOutputCol("features")
//
//    val lr = new LogisticRegression()
//    println("LogisticRegression parameters:\n" + lr.explainParams() + "\n")
//
//    lr.setMaxIter(10)
//      .setRegParam(0.01)
//
//    val pipeline = new Pipeline()
//        .setStages(Array(
//
//          featuresAssembler,
//          lr))
//
//    val model = pipeline.fit(training)

//    val t1 = predOneTransformA.transform(test)
//    val t1dot5 = predOneTransformB.transform(t1)
//    val t2 = predTwoTransformA.transform(t1dot5)
//    val t3 = predTwoTransformB.transform(t2)
//    val t3dot5 = predTwoTransformC.transform(t3)
//    val t4 = featuresAssembler.transform(t3dot5)
//    val t5 = labelTransformA.transform(t4)
//    val t6 = labelTransformB.transform(t5)
//    val t7 = labelTransformC.transform(t6)
//
//    t7.show()
//    t7.drop("o").drop("c").drop("l").drop("h").drop("atr").drop("volume").drop("trading_time")
//      .drop("isMax-c").drop("isMax-c-o-range").drop("o-c-change").drop("o-l-range")
//    println("testInput")
//    t7.show()

//
//    val testResults = model.transform(test)
//
//    testResults.show()
//    println(model.toString())

//    testResults.select("lead-o-l-range-1", "lead-o-h-range-1", "avg-o-c-range-3", "rr-ratio","loss-rr-ratio", "label").show()
//
//
//    println("count correct")
//    val numCorrect = testResults.select("trading_date","features", "label", "probability", "prediction")
//      .collect()
//      .count { case Row(date: Int, features: Vector, label: Double, prob: Vector, prediction: Double) =>
//        label == prediction
//      }
//    val numCorrect = testResults.select("trading_date","features", "label", "prediction")
//      .collect()
//      .count { case Row(date: Int, features: Vector, label: Double, prediction: Double) =>
//        label == prediction
//      }
//
//    val avgRR = testResults.filter(col("label") === 1.0)
//
//      .select(avg("rr-ratio"))
//
//    avgRR.show()
//
//    val avgLossRr = testResults.filter(col("label") === 1.0)
//      .select(avg("loss-rr-ratio"))
//
//    avgLossRr.show()
//
//    val avgThings = testResults.filter(col("label") === 1.0)
//        .select(avg("rr-ratio") / avg("loss-rr-ratio"))
//
//    avgThings.show()
//
//    val expectation = 0.07 * 13.6 - 0.93
//    val rawPredictionThing = testResults.select("rawPrediction")
//      .collect()
//      .count { case Row(rawPrediction: Vector) =>
//        rawPrediction(0) < 0.5
//      }
//    println(s"rawPrediction... $rawPredictionThing")
//
//    val numNatural = testResults.filter(col("label") === 1.0).count()
//    println(s"numCorrect: $numCorrect, numTotal: $testSize, pctCorrect: ${numCorrect.toDouble / testSize.toDouble}")
//    println(s"numNatural: $numNatural, pctNatural: ${numNatural.toDouble / testSize.toDouble}")
//
//    println("filtering...")
//    println(testResults.filter(col("label") === 1.0).count())
//    println(testResults.filter(col("prediction") === 1.0).count())
//    println(testResults.filter(col("label") === 0.0).count())
//
//    val truePositives = testResults.filter(col("label") === 1.0 && col("prediction") === 1.0).count()
//    val falsePositives = testResults.filter(col("label") === 0.0 && col("prediction") === 1.0).count()
//    val precision = truePositives.toDouble / (truePositives.toDouble + falsePositives.toDouble)
//    println(s"tp: $truePositives, fp: $falsePositives, precision $precision")
//
//    val bce = new BinaryClassificationEvaluator()
//      .setLabelCol("label")
//      .setRawPredictionCol("prediction")
//      .setMetricName("areaUnderPR")
//
//    val metric = bce.evaluate(testResults)
//    println(s"metric: $metric")

//      .foreach { case Row(date: Int, features: Vector, label: Double, prob: Vector, prediction: Double) =>
//        println(s"($date, $features) --> prob=$prob, prediction=$prediction, correct=${label == prediction}")
//      }
//
//    val model2 = pipeline.fit(training2)
//    val testResults2 = model2.transform(test2)
//    val testSize2 = test2.count()
//    println("count correct")
//    val numCorrect2 = testResults2.select("trading_date","features", "label", "probability", "prediction")
//      .collect()
//      .count { case Row(date: Int, features: Vector, label: Double, prob: Vector, prediction: Double) =>
//        label == prediction
//      }
//
//    val numNatural2 = testResults2.select("label").collect()
//      .count { case Row(label: Double) =>
//        label == 1.0
//      }
//    println(s"numCorrect: $numCorrect2, numTotal: $testSize2, pctCorrect: ${numCorrect2.toDouble / testSize2.toDouble}")
//    println(s"numNatural: $numNatural2, pctNatural: ${numNatural2.toDouble / testSize2.toDouble}")
//

//    val cv = new CrossValidator()
//      .setEstimator(pipeline)
//      .setEvaluator(new BinaryClassificationEvaluator())
//      .setEstimatorParamMaps(new ParamGridBuilder().build())
//      .setNumFolds(3)
//
//    val cvModel = cv.fit(training)
//    val cvResult = cvModel.transform(test)
//
//    val cvNumCorrect = cvResult.select("trading_date","features", "label", "probability", "prediction")
//      .collect()
//      .count { case Row(date: Int, features: Vector, label: Double, prob: Vector, prediction: Double) =>
//        label == prediction
//      }
//
//    println(s"numCorrect: $cvNumCorrect, numTotal: $testSize, pctCorrect: ${cvNumCorrect.toDouble / testSize.toDouble}")
//    println(s"numNatural: $numNatural, pctNatural: ${numNatural.toDouble / testSize.toDouble}")
    /*
    ml models
     */



//    val model1 = lr.fit(transformedDf)
//    println("Model 1 was fit using parameters: " + model1.parent.extractParamMap)
//
//    val test = sparkSession.createDataFrame(Seq(
//      (0.0, Vectors.dense(32, 33, 34, 35)),
//      (1.0, Vectors.dense(980, 960, 1000, 1010)),
//      (0.0, Vectors.dense(1,2,3,4)),
//      (1.0, Vectors.dense(981, 961, 1001, 1011)),
//      (1.0, Vectors.dense(5,6,7,900))
//    )).toDF("label", "features")
//
//    model1.transform(test)
//      .select("features", "label", "probability", "prediction")
//      .collect()
//      .foreach { case Row(features: Vector, label: Double, prob: Vector, prediction: Double) =>
//        println(s"($features, $label) -> prob=$prob, prediction=$prediction, correct=${label.toInt == prediction.toInt}")
//      }


    // ------------------------

    //    val examples = MLUtils.loadLibSVMFile(sc, inputFile).cache()
//
//    val splits = examples.randomSplit(Array(0.8, 0.2))
//
//    val training = splits(0).cache()
//    val test = splits(1).cache()
//
//    val numTraining = training.count()
//    val numTest = test.count()
//    println(s"Training: $numTraining, test: $numTest.")
//
//    examples.unpersist(blocking = false)
//
//    val updater = new SquaredL2Updater()
//
//    val model = {
//      val algorithm = new LogisticRegressionWithLBFGS()
//      algorithm.optimizer
//        .setNumIterations(100)
//        .setUpdater(updater)
//        .setRegParam(0.01)
//      algorithm.run(training).clearThreshold()
//    }
//
//    val prediction = model.predict(test.map(_.features))
//    val predictionAndLabel = prediction.zip(test.map(_.label))
//
//    val metrics = new BinaryClassificationMetrics(predictionAndLabel)
//
//    println(s"Test areaUnderPR = ${metrics.areaUnderPR()}.")
//    println(s"Test areaUnderROC = ${metrics.areaUnderROC()}.")
  }
}
