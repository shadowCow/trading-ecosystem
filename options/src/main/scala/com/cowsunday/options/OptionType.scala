package com.cowsunday.options

object OptionType extends Enumeration {
  val CALL, PUT = Value;
  
  def getMultiplier(optionType: OptionType.Value): Int = {
    if (optionType == OptionType.CALL) {
      1
    } else {
      -1
    }
  }
}