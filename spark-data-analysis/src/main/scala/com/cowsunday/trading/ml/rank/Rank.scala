package com.cowsunday.trading.ml.rank

import org.apache.spark.rdd.RDD

object Ranking {

  def toRanks(values: RDD[Double]): RDD[Long] = {
    // sorted lowest value to highest value, indexes attached
    val sorted = values.zipWithIndex.sortBy(x => x._1)

    // list of tuples: (original indexes -> rank)
    val ranks = sorted.map(x => x._2).zipWithIndex

    // reorder by original index, map to rank value
    ranks.sortBy(x => x._1).map(x => x._2)
  }

  def toRanksInt(values: RDD[Int]): RDD[Long] = {
    // sorted lowest value to highest value, indexes attached
    val sorted = values.zipWithIndex.sortBy(x => x._1)

    // list of tuples: (original indexes -> rank)
    val ranks = sorted.map(x => x._2).zipWithIndex

    // reorder by original index, map to rank value
    ranks.sortBy(x => x._1).map(x => x._2)
  }

}