package com.cowsunday.options

import org.scalatest.Assertions
import org.junit.Test
import org.junit.Before
import org.junit.After
import java.time._

class OptionLegTest extends Assertions {

  val call = new Option("spy", OptionType.CALL, 100, Instant.now)
  val put = new Option("spy", OptionType.PUT, 100, Instant.now)
  val longCall = new OptionLeg(call, 2, 10, PositionDirection.LONG, 100)
  val shortCall = new OptionLeg(call, 2, 10, PositionDirection.SHORT, 100)
  val longPut = new OptionLeg(put, 2, 10, PositionDirection.LONG, 100)
  val shortPut = new OptionLeg(put, 2, 10, PositionDirection.SHORT, 100)
  
  @Test def testGetValue() {
    assert(longCall.getValue(3) == 1000)
    assert(longCall.getValue(1) == -1000)
    assert(longCall.getValue(2) == 0)
    
    assert(shortCall.getValue(3) == -1000)
    assert(shortCall.getValue(1) == 1000)
    assert(shortCall.getValue(2) == 0)
    
    assert(longPut.getValue(3) == 1000)
    assert(longPut.getValue(1) == -1000)
    assert(longPut.getValue(2) == 0)
    
    assert(shortPut.getValue(3) == -1000)
    assert(shortPut.getValue(1) == 1000)
    assert(shortPut.getValue(2) == 0)
        
  }
  
  @Test def testGetValueAtExpiration() {
    // itm
    assert(longCall.getValueAtExpiration(105) == 3000)
    assert(shortCall.getValueAtExpiration(105) == -3000)
    assert(longPut.getValueAtExpiration(95) == 3000)
    assert(shortPut.getValueAtExpiration(95) == -3000)
    
    // atm
    assert(longCall.getValueAtExpiration(100) == -2000)
    assert(shortCall.getValueAtExpiration(100) == 2000)
    assert(longPut.getValueAtExpiration(100) == -2000)
    assert(shortPut.getValueAtExpiration(100) == 2000)
    
    // otm
    assert(longCall.getValueAtExpiration(95) == -2000)
    assert(shortCall.getValueAtExpiration(95) == 2000)
    assert(longPut.getValueAtExpiration(105) == -2000)
    assert(shortPut.getValueAtExpiration(105) == 2000)
    
    // breakeven
    assert(longCall.getValueAtExpiration(102) == 0)
    assert(shortCall.getValueAtExpiration(102) == 0)
    assert(longPut.getValueAtExpiration(98) == 0)
    assert(shortPut.getValueAtExpiration(98) == 0)
  }
  
}