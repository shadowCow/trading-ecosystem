package com.cowsunday.options

trait Distribution {
  def cumulativeProbability(x: Double): Double
  def probability(x: Double): Double
}