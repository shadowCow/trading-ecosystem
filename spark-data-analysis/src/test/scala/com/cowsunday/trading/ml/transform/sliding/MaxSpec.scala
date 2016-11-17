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
import com.cowsunday.trading.ml.data.PriceBar
import com.cowsunday.trading.ml.data.PriceType
import com.cowsunday.trading.ml.transform._
import com.cowsunday.trading.ml.transform.bar._

@RunWith(classOf[JUnitRunner])
class MaxSpec extends Specification with SparkBeforeAfter {
  override def appName = "Max Test"

  val priceBars = Seq(new PriceBar(4,5,0.5,3,20150101),
        new PriceBar(4,4,1.5,2.5,20150102),
        new PriceBar(3,6,2.5,4,20150103),
        new PriceBar(2,3,1,1.5, 20150104),
        new PriceBar(1,1.5,0.5,1, 20150105))
  val rdd = sc.parallelize(priceBars)

  val transforms = new Transforms()
  val barTransforms = new BarTransforms()
  val highRdd = transforms.transformPriceData(rdd)(barTransforms.high)
  val openRdd = transforms.transformPriceData(rdd)(barTransforms.open)

  "Max" should {
    "have correct values" in {
      val length = 3

      val maxHigh = new SlidingTransforms().max
      val highBars = transforms.transformWindow(highRdd, length)(maxHigh).take(3)

      highBars(0) mustEqual 6
      highBars(1) mustEqual 6
      highBars(2) mustEqual 6

      val maxOpen = new SlidingTransforms().max
      val openBars = transforms.transformWindow(openRdd, length)(maxOpen).take(3)

      openBars(0) mustEqual 4
      openBars(1) mustEqual 4
      openBars(2) mustEqual 3
    }
  }
}