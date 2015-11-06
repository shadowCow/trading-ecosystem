package com.cowsunday.sparkdataanalysis.transform

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.scalatest.Assertions
import org.junit.Test
import org.junit.Before
import org.junit.After

import com.cowsunday.sparkdataanalysis.data.PriceBar

class FirstUpBarTest extends Assertions {

  private val master = "local[2]"
  private val appName = "FirstUpBarTest-test"
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
    val priceBars = Seq(new PriceBar(4,2,0.5,3,20150101),  
        new PriceBar(4,4,1.5,2.5,20150102), 
        new PriceBar(3,6,2.5,4,20150103),
        new PriceBar(4,7,3.5,10,20150104),
        new PriceBar(6,8,4.5,5.5,20150105),
        new PriceBar(20,9,5.5,13,20150106),
        new PriceBar(13,10,6.5,9,20150107))
    val rdd = sc.parallelize(priceBars)
    
    val results = FirstUpBar.transform(rdd, 3).take(5)
    
    assert(results(0) == 1)
    assert(results(1) == 0)
    assert(results(2) == 0)
    assert(results(3) == 0)
    assert(results(4) == 0)
  }
}