package com.cowsunday.trading.ml.transform.bar

import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.mutable._
import org.specs2.runner.JUnitRunner
import org.specs2.matcher.{ Expectable, Matcher }
import com.cowsunday.trading.ml.SparkBeforeAfter
import com.cowsunday.trading.ml.data.PriceBar
import com.cowsunday.trading.ml.transform.Transforms
import com.cowsunday.trading.ml.transform.bar._

@RunWith(classOf[JUnitRunner])
class BarTransformSpec extends Specification with SparkBeforeAfter {
  override def appName = "Bar Transform Test"

  val priceBars = Seq(new PriceBar(4,5,0.5,3,20150101),
        new PriceBar(4,4,1.5,2.5,20150102),
        new PriceBar(3,6,2.5,4,20150103))
    val rdd = sc.parallelize(priceBars)

  "Bar transforms" should {
    "transform to high" in {
      val transform = new BarTransforms().high
      val results = new Transforms().transformPriceData(rdd)(transform).take(3)

      results(0) must beCloseTo(5, 0.0001)
      results(1) must beCloseTo(4, 0.0001)
      results(2) must beCloseTo(6, 0.0001)
    }

    "transform to low" in {
      val transform = new BarTransforms().low
      val results = new Transforms().transformPriceData(rdd)(transform).take(3)

      results(0) must beCloseTo(0.5, 0.0001)
      results(1) must beCloseTo(1.5, 0.0001)
      results(2) must beCloseTo(2.5, 0.0001)
    }

    "tranform to close" in {
      val transform = new BarTransforms().close
      val results = new Transforms().transformPriceData(rdd)(transform).take(3)

      results(0) must beCloseTo(3, 0.0001)
      results(1) must beCloseTo(2.5, 0.0001)
      results(2) must beCloseTo(4, 0.0001)
    }

    "transform to open" in {
      val transform = new BarTransforms().open
      val results = new Transforms().transformPriceData(rdd)(transform).take(3)

      results(0) must beCloseTo(4, 0.0001)
      results(1) must beCloseTo(4, 0.0001)
      results(2) must beCloseTo(3, 0.0001)
    }

    "transform to close high range" in {
      val transform = new BarTransforms().closeHighRange
      val results = new Transforms().transformPriceData(rdd)(transform).take(3)

      results(0) must beCloseTo(2, 0.0001)
      results(1) must beCloseTo(1.5, 0.0001)
      results(2) must beCloseTo(2, 0.0001)
    }

    "transform to close low range" in {
      val transform = new BarTransforms().closeLowRange
      val results = new Transforms().transformPriceData(rdd)(transform).take(3)

      results(0) must beCloseTo(2.5, 0.0001)
      results(1) must beCloseTo(1, 0.0001)
      results(2) must beCloseTo(1.5, 0.0001)
    }

    "transform to hi low range" in {
      val transform = new BarTransforms().hiLowRange
      val results = new Transforms().transformPriceData(rdd)(transform).take(3)

      results(0) must beCloseTo(4.5, 0.0001)
      results(1) must beCloseTo(2.5, 0.0001)
      results(2) must beCloseTo(3.5, 0.0001)
    }

    "transform to open close change" in {
      val transform = new BarTransforms().openCloseChange
      val results = new Transforms().transformPriceData(rdd)(transform).take(3)

      results(0) must beCloseTo(-1, 0.0001)
      results(1) must beCloseTo(-1.5, 0.0001)
      results(2) must beCloseTo(1, 0.0001)
    }

    "transform to open close range" in {
      val transform = new BarTransforms().openCloseRange
      val results = new Transforms().transformPriceData(rdd)(transform).take(3)

      results(0) must beCloseTo(1, 0.0001)
      results(1) must beCloseTo(1.5, 0.0001)
      results(2) must beCloseTo(1, 0.0001)
    }

    "transform to open high range" in {
      val transform = new BarTransforms().openHighRange
      val results = new Transforms().transformPriceData(rdd)(transform).take(3)

      results(0) must beCloseTo(1, 0.0001)
      results(1) must beCloseTo(0, 0.0001)
      results(2) must beCloseTo(3, 0.0001)
    }

    "transform to open low range" in {
      val transform = new BarTransforms().openLowRange
      val results = new Transforms().transformPriceData(rdd)(transform).take(3)

      results(0) must beCloseTo(3.5, 0.0001)
      results(1) must beCloseTo(2.5, 0.0001)
      results(2) must beCloseTo(0.5, 0.0001)
    }
  }
}