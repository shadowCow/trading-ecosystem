package com.cowsunday.options

object PositionDirection {
  sealed abstract class Direction(val positionMultiplier: Int, val name: String) {
    override def toString = name
  }
  
  case object SHORT extends Direction(-1, "Short")
  case object FLAT extends Direction(0, "Flat")
  case object LONG extends Direction(1, "Long")
}