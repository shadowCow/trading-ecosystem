package com.cowsunday.trading.ml.feature

import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.mutable._
import org.specs2.runner.JUnitRunner
import org.specs2.matcher.{ Expectable, Matcher }
import com.cowsunday.trading.ml.SparkBeforeAfter
import com.cowsunday.trading.ml.data.PriceBar
import com.cowsunday.trading.ml.transform._
import com.cowsunday.trading.ml.transform.bar._
import com.cowsunday.trading.ml.transform.sliding._

@RunWith(classOf[JUnitRunner])
class FeatureSelectorSpec extends Specification with SparkBeforeAfter {

  override def appName = "Feature Selector Test"

  val priceBars = Seq(new PriceBar(4,5,0.5,3,20150101),
        new PriceBar(4,4,1.5,2.5,20150102),
        new PriceBar(3,6,2.5,4,20150103),
        new PriceBar(3.5, 5.5, 3.0, 4.5, 20150104),
        new PriceBar(4.5, 6.5, 4.5, 5.0, 20150105))

  val rdd = sc.parallelize(priceBars)

  val barTransforms = Seq[PriceBar => Double](
      new BarTransforms().open,
      new BarTransforms().close
      )

  val slidingTransforms = Seq[Array[Double] => Double](
      new SlidingTransforms().max,
      new SlidingTransforms().min
      )

  val lengths = Seq[Int](
      2, 3
      )

  "Feature Selector" should {

    "get the correct next feature" in {
      val featureSelector = new FeatureSelector(barTransforms, slidingTransforms, lengths, new Transforms())

      // get the valid features
      val feature1 = featureSelector.nextFeature(rdd)
      val feature2 = featureSelector.nextFeature(rdd)
      val feature3 = featureSelector.nextFeature(rdd)
      val feature4 = featureSelector.nextFeature(rdd)
      val feature5 = featureSelector.nextFeature(rdd)
      val feature6 = featureSelector.nextFeature(rdd)
      val feature7 = featureSelector.nextFeature(rdd)
      val feature8 = featureSelector.nextFeature(rdd)

      // make sure we get to the end of the feature list at the right time
      featureSelector.nextFeature(rdd) must throwA[EndOfFeatureListException]

      // validate the content of the generated features
      feature1.count() mustEqual 4
      val feature1Array = feature1.take(4)
      feature1Array(0) mustEqual 4
      feature1Array(1) mustEqual 4
      feature1Array(2) mustEqual 3.5
      feature1Array(3) mustEqual 4.5

      feature2.count() mustEqual 3
      val feature2Array = feature2.take(3)
      feature2Array(0) mustEqual 4
      feature2Array(1) mustEqual 4
      feature2Array(2) mustEqual 4.5

      feature3.count() mustEqual 4
      val feature3Array = feature3.take(4)
      feature3Array(0) mustEqual 4
      feature3Array(1) mustEqual 3
      feature3Array(2) mustEqual 3
      feature3Array(3) mustEqual 3.5

      feature4.count() mustEqual 3
      val feature4Array = feature4.take(3)
      feature4Array(0) mustEqual 3
      feature4Array(1) mustEqual 3
      feature4Array(2) mustEqual 3

      feature5.count() mustEqual 4
      val feature5Array = feature5.take(4)
      feature5Array(0) mustEqual 3
      feature5Array(1) mustEqual 4
      feature5Array(2) mustEqual 4.5
      feature5Array(3) mustEqual 5

      feature6.count() mustEqual 3
      val feature6Array = feature6.take(3)
      feature6Array(0) mustEqual 4
      feature6Array(1) mustEqual 4.5
      feature6Array(2) mustEqual 5

      feature7.count() mustEqual 4
      val feature7Array = feature7.take(4)
      feature7Array(0) mustEqual 2.5
      feature7Array(1) mustEqual 2.5
      feature7Array(2) mustEqual 4
      feature7Array(3) mustEqual 4.5

      feature8.count() mustEqual 3
      val feature8Array = feature8.take(3)
      feature8Array(0) mustEqual 2.5
      feature8Array(1) mustEqual 2.5
      feature8Array(2) mustEqual 4

    }

  }

}