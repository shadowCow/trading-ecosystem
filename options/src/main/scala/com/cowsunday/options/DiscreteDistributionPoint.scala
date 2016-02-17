package com.cowsunday.options

case class DiscreteDistributionPoint(val value: Double, val probability: Double) {
  if (probability > 1.0 || probability < 0.0) {
    throw new IllegalArgumentException("probability must be 0.0 < p < 1.0");
  }
}