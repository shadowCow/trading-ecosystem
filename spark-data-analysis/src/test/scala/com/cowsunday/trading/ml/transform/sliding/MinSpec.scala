package com.cowsunday.trading.ml.transform.sliding

import org.junit.Test
import org.junit.Before
import org.junit.After
import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.mutable._
import org.specs2.runner.JUnitRunner
import org.specs2.matcher.{ Expectable, Matcher }
import com.cowsunday.trading.ml.SparkBeforeAfter
import com.cowsunday.sparkdataanalysis.data.PriceBar
import com.cowsunday.sparkdataanalysis.data.PriceType
import com.cowsunday.trading.ml.transform.bar._

@RunWith(classOf[JUnitRunner])
class MinSpec extends Specification with SparkBeforeAfter {

  override def appName = "Min Test"

  val priceBars = Seq(new PriceBar(4,5,0.5,3,20150101),
        new PriceBar(4,4,1.5,2.5,20150102),
        new PriceBar(3,6,2.5,4,20150103),
        new PriceBar(2,3,1,1.5, 20150104),
        new PriceBar(1,1.5,0.5,1, 20150105))
  val rdd = sc.parallelize(priceBars)

  val lowRdd = new Low().transform(rdd)
  val closeRdd = new Close().transform(rdd)

  "Min" should {
    "have correct values" in {
      val length = 3

      val minLow = new Min
      val lowBars = minLow.transform(lowRdd, length).take(3)

      lowBars(0) mustEqual 0.5
      lowBars(1) mustEqual 1
      lowBars(2) mustEqual 0.5

      val minClose = new Min
      val closeBars = minClose.transform(closeRdd, length).take(3)

      closeBars(0) mustEqual 2.5
      closeBars(1) mustEqual 1.5
      closeBars(2) mustEqual 1
    }
  }
}