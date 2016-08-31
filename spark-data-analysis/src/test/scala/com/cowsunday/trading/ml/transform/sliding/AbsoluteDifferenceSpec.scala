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
class AbsoluteDifferenceSpec extends Specification with SparkBeforeAfter {

  override def appName = "Absolute Difference Test"

  val priceBars = Seq(new PriceBar(4,5,0.5,3,20150101),
        new PriceBar(4,4,1.5,2.5,20150102),
        new PriceBar(3,6,2.5,4,20150103),
        new PriceBar(2,3,1,1.5, 20150104),
        new PriceBar(1,1.5,0.5,1, 20150105))
  val rdd = sc.parallelize(priceBars)

  val doubleRdd = new Close().transform(rdd)

  "Absolute Difference" should {
    "have correct values for length 3" in {
      val length = 3

      val closeClose = new AbsoluteDifference
      val ccdiff = closeClose.transform(doubleRdd, length).take(3)

      ccdiff(0) mustEqual 1
      ccdiff(1) mustEqual 1
      ccdiff(2) mustEqual 3

    }

    "have correct values for length 2" in {
      val length = 2

      val closeClose = new AbsoluteDifference
      val ccdiff = closeClose.transform(doubleRdd, length).take(4)

      ccdiff(0) mustEqual 0.5
      ccdiff(1) mustEqual 1.5
      ccdiff(2) mustEqual 2.5
      ccdiff(3) mustEqual 0.5

    }
  }

}