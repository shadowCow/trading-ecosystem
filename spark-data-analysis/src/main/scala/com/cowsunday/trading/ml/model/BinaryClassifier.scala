package com.cowsunday.trading.ml.model

import org.apache.spark.SparkContext
import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
import org.apache.spark.mllib.optimization.SquaredL2Updater
import org.apache.spark.mllib.util.MLUtils

class BinaryClassifier {

  def run(sc: SparkContext, inputFile: String): Unit = {
    val examples = MLUtils.loadLibSVMFile(sc, inputFile).cache()

    val splits = examples.randomSplit(Array(0.8, 0.2))

    val training = splits(0).cache()
    val test = splits(1).cache()

    val numTraining = training.count()
    val numTest = test.count()
    println(s"Training: $numTraining, test: $numTest.")

    examples.unpersist(blocking = false)

    val updater = new SquaredL2Updater()

    val model = {
      val algorithm = new LogisticRegressionWithLBFGS()
      algorithm.optimizer
        .setNumIterations(100)
        .setUpdater(updater)
        .setRegParam(0.01)
      algorithm.run(training).clearThreshold()
    }

    val prediction = model.predict(test.map(_.features))
    val predictionAndLabel = prediction.zip(test.map(_.label))

    val metrics = new BinaryClassificationMetrics(predictionAndLabel)

    println(s"Test areaUnderPR = ${metrics.areaUnderPR()}.")
    println(s"Test areaUnderROC = ${metrics.areaUnderROC()}.")
  }
}
