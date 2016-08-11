package com.cowsunday.sparkdataanalysis.transform

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

class UpBarStreakTest extends Specification {

  private val master = "local[2]"
  private val appName = "UpBarStreakTest-test"
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

  @Test def testUpBarStreak() {
    val priceBars = Seq(new PriceBar(1,2,0.5,1.5,20150101),
        new PriceBar(2,4,1.5,1,20150102),
        new PriceBar(3,6,2.5,4,20150103),
        new PriceBar(4,7,3.5,5,20150104),
        new PriceBar(5,8,4.5,6,20150105),
        new PriceBar(6,9,5.5,3,20150106),
        new PriceBar(7,10,6.5,2,20150107))
    val rdd = sc.parallelize(priceBars)

    val results = UpBarStreak.transform(rdd).take(7)

    assert(results(0) === 1)
    assert(results(1) === 0)
    assert(results(2) === 1)
    assert(results(3) === 2)
    assert(results(4) === 3)
    assert(results(5) === 0)
    assert(results(6) === 0)
  }
}