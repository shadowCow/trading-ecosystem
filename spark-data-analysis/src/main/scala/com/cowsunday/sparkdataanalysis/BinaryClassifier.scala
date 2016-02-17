package com.cowsunday.sparkdataanalysis

import org.apache.spark.{ SparkConf, SparkContext }
import org.apache.spark.mllib.classification.{LogisticRegressionWithLBFGS, SVMWithSGD}
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.mllib.optimization.{SquaredL2Updater, L1Updater}

object BinaryClassifier {

  def classify(sc: SparkContext) {
    
    val examples = MLUtils.loadLibSVMFile(sc, "src/main/resources/classification_data.txt").cache()
    
    val splits = examples.randomSplit(Array(0.8,0.2))
    val training = splits(0).cache()
    val test = splits(1).cache()
    
    val numTraining = training.count()
    val numTest = test.count()
    println(s"Training: $numTraining, test: $numTest.")
    
    examples.unpersist(blocking = false)
    
    val updater = new L1Updater()
    val algorithm = new SVMWithSGD()
    algorithm.optimizer
      .setNumIterations(100)
      .setStepSize(1.0)
      .setUpdater(updater)
      .setRegParam(1.0)
    val model = algorithm.run(training).clearThreshold()
    
    val prediction = model.predict(test.map(_.features))
    val predictionAndLabel = prediction.zip(test.map(_.label))
    
    val metrics = new BinaryClassificationMetrics(predictionAndLabel)
    
    println(s"Test areaUnderPR = ${metrics.areaUnderPR()}.")
    println(s"Test areaUnderROC = ${metrics.areaUnderROC()}")
    
    sc.stop()
  }
}