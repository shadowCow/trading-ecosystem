package com.cowsunday.trading.ml.rank

import org.junit.Test
import org.junit.Before
import org.junit.After
import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.mutable._
import org.specs2.runner.JUnitRunner
import org.specs2.matcher.{ Expectable, Matcher }
import com.cowsunday.trading.ml.SparkBeforeAfter

@RunWith(classOf[JUnitRunner])
class RankingSpec extends Specification with SparkBeforeAfter {

  override def appName = "Ranking Test"

  "Ranking" should {

    "correctly rank a list of doubles already sorted ascending" in {
      val valuesA = sc.parallelize(Seq(-1.0, 0.0, 3.0, 4.0, 5.0))
      val ranksA = Ranking.toRanks(valuesA).take(5)
      ranksA(0) mustEqual 0
      ranksA(1) mustEqual 1
      ranksA(2) mustEqual 2
      ranksA(3) mustEqual 3
      ranksA(4) mustEqual 4
    }

    "correctly rank a list of doubles already sorted descending" in {
      val valuesD = sc.parallelize(Seq(5.0, 4.0, 3.0, 0.0, -1.0))
      val ranksD = Ranking.toRanks(valuesD).take(5)
      ranksD(0) mustEqual 4
      ranksD(1) mustEqual 3
      ranksD(2) mustEqual 2
      ranksD(3) mustEqual 1
      ranksD(4) mustEqual 0
    }

    "correctly rank an unsorted list of doubles" in {
      val valuesM = sc.parallelize(Seq(3.0, 0.0, 4.0, -1.0, 5.0))
      val ranksM = Ranking.toRanks(valuesM).take(5)
      ranksM(0) mustEqual 2
      ranksM(1) mustEqual 1
      ranksM(2) mustEqual 3
      ranksM(3) mustEqual 0
      ranksM(4) mustEqual 4
    }

    "correctly rank a list of ints already sorted ascending" in {
      val valuesA = sc.parallelize(Seq(-1, 0, 3, 4, 5))
      val ranksA = Ranking.toRanksInt(valuesA).take(5)
      ranksA(0) mustEqual 0
      ranksA(1) mustEqual 1
      ranksA(2) mustEqual 2
      ranksA(3) mustEqual 3
      ranksA(4) mustEqual 4
    }

    "correctly rank a list of ints already sorted descending" in {
      val valuesD = sc.parallelize(Seq(5, 4, 3, 0, -1))
      val ranksD = Ranking.toRanksInt(valuesD).take(5)
      ranksD(0) mustEqual 4
      ranksD(1) mustEqual 3
      ranksD(2) mustEqual 2
      ranksD(3) mustEqual 1
      ranksD(4) mustEqual 0
    }

    "correctly rank an unsorted list of ints" in {
      val valuesM = sc.parallelize(Seq(3, 0, 4, -1, 5))
      val ranksM = Ranking.toRanksInt(valuesM).take(5)
      ranksM(0) mustEqual 2
      ranksM(1) mustEqual 1
      ranksM(2) mustEqual 3
      ranksM(3) mustEqual 0
      ranksM(4) mustEqual 4
    }

  }

}