package com.cowsunday.sparkdataanalysis.transform

import org.scalatest.Assertions
import org.junit.Test
import org.junit.Before
import org.junit.After
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import com.cowsunday.sparkdataanalysis.data.PriceBar
import com.cowsunday.sparkdataanalysis.data.PriceType

class SimpleMovingAverageTransformTest extends Assertions {

  private val master = "local[2]"
  private val appName = "sma-transform-test"
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
    val priceBars = Seq(new PriceBar(1,2,0.5,1.5,20150101), 
        new PriceBar(2,4,1.5,2.5,20150102), 
        new PriceBar(3,6,2.5,3.5,20150103),
        new PriceBar(4,7,3.5,4.5,20150104),
        new PriceBar(5,8,4.5,5.5,20150105),
        new PriceBar(6,9,5.5,6.5,20150106),
        new PriceBar(7,10,6.5,7.5,20150107))
    val rdd = sc.parallelize(priceBars)
    
    val smas = SimpleMovingAverageTransform.transform(rdd, 3, PriceType.Close)
    
    val values = smas.take(5)
    println(values(0))
    println(values(1))
    println(values(2))
    println(values(3))
    println(values(4))
    
    assert(values(0) === 2.5)
    assert(values(1) === 3.5)
    assert(values(2) === 4.5)
    assert(values(3) === 5.5)
    assert(values(4) === 6.5)
    
  }
  
}