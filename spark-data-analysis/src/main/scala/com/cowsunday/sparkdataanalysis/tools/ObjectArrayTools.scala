package com.cowsunday.sparkdataanalysis.tools

class ObjectArrayTools[T <: AnyRef](a: Array[T]) {
  def binarySearch(key: T) = {
    java.util.Arrays.binarySearch(a.asInstanceOf[Array[AnyRef]],key)
  }
}
