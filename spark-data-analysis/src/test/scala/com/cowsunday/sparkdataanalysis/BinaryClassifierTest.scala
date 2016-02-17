package com.cowsunday.sparkdataanalysis

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.scalatest.Assertions
import org.junit.Test
import org.junit.Before
import org.junit.After

import com.cowsunday.sparkdataanalysis.data.PriceBar

class BinaryClassifierTest extends Assertions {

  private val master = "local[2]"
  private val appName = "BinaryClassifierTest-test"
  private val sparkHome = "/usr/local/spark-1.4.1"
  private val jars = Array[String]("/Users/dwadeson/trading-ecosystem/spark-data-analysis/target/spark-data-analysis-0.0.1-SNAPSHOT.jar")
  
  private var sc: SparkContext = _
  
  @Before def setup() {
    val conf = new SparkConf()
                  .setMaster(master)
                  .setAppName(appName)
                  .setSparkHome(sparkHome)
                  .setJars(jars)
                  
    sc = new SparkContext(conf)
  }
  
  @After def after() {
    if (sc != null) {
      sc.stop()
    }
  }
  
  @Test def testClassifier() {
    BinaryClassifier.classify(sc)
  }
}