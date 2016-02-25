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
  
  @Test def testgetNetGain() {
    assert(longCall.getNetGain(3) == 1000)
    assert(longCall.getNetGain(1) == -1000)
    assert(longCall.getNetGain(2) == 0)
    
    assert(shortCall.getNetGain(3) == -1000)
    assert(shortCall.getNetGain(1) == 1000)
    assert(shortCall.getNetGain(2) == 0)
    
    assert(longPut.getNetGain(3) == 1000)
    assert(longPut.getNetGain(1) == -1000)
    assert(longPut.getNetGain(2) == 0)
    
    assert(shortPut.getNetGain(3) == -1000)
    assert(shortPut.getNetGain(1) == 1000)
    assert(shortPut.getNetGain(2) == 0)
        
  }
  
  @Test def testgetNetGainAtExpiration() {
    // itm
    assert(longCall.getNetGainAtExpiration(105) == 3000)
    assert(shortCall.getNetGainAtExpiration(105) == -3000)
    assert(longPut.getNetGainAtExpiration(95) == 3000)
    assert(shortPut.getNetGainAtExpiration(95) == -3000)
    
    // atm
    assert(longCall.getNetGainAtExpiration(100) == -2000)
    assert(shortCall.getNetGainAtExpiration(100) == 2000)
    assert(longPut.getNetGainAtExpiration(100) == -2000)
    assert(shortPut.getNetGainAtExpiration(100) == 2000)
    
    // otm
    assert(longCall.getNetGainAtExpiration(95) == -2000)
    assert(shortCall.getNetGainAtExpiration(95) == 2000)
    assert(longPut.getNetGainAtExpiration(105) == -2000)
    assert(shortPut.getNetGainAtExpiration(105) == 2000)
    
    // breakeven
    assert(longCall.getNetGainAtExpiration(102) == 0)
    assert(shortCall.getNetGainAtExpiration(102) == 0)
    assert(longPut.getNetGainAtExpiration(98) == 0)
    assert(shortPut.getNetGainAtExpiration(98) == 0)
  }
  
}