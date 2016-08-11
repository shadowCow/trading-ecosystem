package com.cowsunday.sparkdataanalysis.transform

import org.junit.Test
import org.junit.Before
import org.junit.After
import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.mutable._
import org.specs2.runner.JUnitRunner
import org.specs2.matcher.{ Expectable, Matcher }
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import com.cowsunday.sparkdataanalysis.data.PriceBar

@RunWith(classOf[JUnitRunner])
class HiLowRangeTransformTest extends Specification {

  private val master = "local[2]"
  private val appName = "hi-low-transform-test"
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

  @Test def testTransform() {
    val priceBars = Seq(new PriceBar(1,2,0.5,1.5,20150101), new PriceBar(2,4,1.5,2.5,20150102), new PriceBar(3,6,2.5,3.5,20150103))
    val rdd = sc.parallelize(priceBars)

    val ranges = HiLowRangeTransform.computeHiLowRange(rdd)

    println(ranges)
    val values = ranges.take(3)
    println(values(0))
    println(values(1))
    println(values(2))
    assert(values(0) === 1.5)
    assert(values(1) === 2.5)
    assert(values(2) === 3.5)
  }

}