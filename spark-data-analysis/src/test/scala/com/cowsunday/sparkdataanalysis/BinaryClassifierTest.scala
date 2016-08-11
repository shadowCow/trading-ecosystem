package com.cowsunday.sparkdataanalysis

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.junit.Test
import org.junit.Before
import org.junit.After
import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.mutable._
import org.specs2.runner.JUnitRunner
import org.specs2.matcher.{ Expectable, Matcher }

import com.cowsunday.sparkdataanalysis.data.PriceBar

@RunWith(classOf[JUnitRunner])
class BinaryClassifierTest extends Specification {

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