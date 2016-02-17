package com.cowsunday.options

import org.scalatest.Assertions
import org.junit.Test
import org.junit.Before
import org.junit.After
import java.time._

class OptionPositionTest extends Assertions {

  @Test def testLongCall() {
    val longCall = OptionPositionFactory.longCall("spy", 100, Instant.now, 2, 10)
    assert(longCall.maxGain == Double.PositiveInfinity)
    assert(longCall.maxLoss == -2000)
    
    // itm
    assert(longCall.getValueAtExpiration(105) == 3000)
    // atm
    assert(longCall.getValueAtExpiration(100) == -2000)
    // otm
    assert(longCall.getValueAtExpiration(95) == -2000)
    // breakeven
    assert(longCall.getValueAtExpiration(102) == 0)
  }
  
  @Test def testShortCall() {
    val shortCall = OptionPositionFactory.shortCall("spy", 100, Instant.now, 2, 10)
    assert(shortCall.maxGain == 2000)
    assert(shortCall.maxLoss == Double.NegativeInfinity)
    
    // itm
    assert(shortCall.getValueAtExpiration(105) == -3000)
    // atm 
    assert(shortCall.getValueAtExpiration(100) == 2000)
    // otm
    assert(shortCall.getValueAtExpiration(95) == 2000)
    // breakeven
    assert(shortCall.getValueAtExpiration(102) == 0)
  }
  
  @Test def testLongPut() {
    val longPut = OptionPositionFactory.longPut("spy", 100, Instant.now, 2, 10)
    assert(longPut.maxGain == 98000)
    assert(longPut.maxLoss == -2000)
    
    // itm
    assert(longPut.getValueAtExpiration(95) == 3000)
    // atm
    assert(longPut.getValueAtExpiration(100) == -2000)
    // otm
    assert(longPut.getValueAtExpiration(105) == -2000)
    // breakeven
    assert(longPut.getValueAtExpiration(98) == 0)
  }
  
  @Test def testShortPut() {
    val shortPut = OptionPositionFactory.shortPut("spy", 100, Instant.now, 2, 10)
    assert(shortPut.maxGain == 2000)
    assert(shortPut.maxLoss == -98000)
    
    // itm
    assert(shortPut.getValueAtExpiration(95) == -3000)
    // atm
    assert(shortPut.getValueAtExpiration(100) == 2000)
    // otm
    assert(shortPut.getValueAtExpiration(105) == 2000)
    // breakeven
    assert(shortPut.getValueAtExpiration(98) == 0)
  }
  
  @Test def testBullCallSpread() {
    val bullCallSpread = OptionPositionFactory.bullCallSpread("spy", 100, 105, Instant.now, 2, 1, 10)
    assert(bullCallSpread.maxGain == 4000, "maxGain was: " + bullCallSpread.maxGain)
    assert(bullCallSpread.maxLoss == -1000)
    
    // below lower strike
    assert(bullCallSpread.getValueAtExpiration(98) == -1000)
    // at lower strike
    assert(bullCallSpread.getValueAtExpiration(100) == -1000)
    // between strikes
    assert(bullCallSpread.getValueAtExpiration(103) == 2000)
    // at upper strike
    assert(bullCallSpread.getValueAtExpiration(105) == 4000)
    // above upper strike
    assert(bullCallSpread.getValueAtExpiration(108) == 4000)
    // break even
    assert(bullCallSpread.getValueAtExpiration(101) == 0)
  }
  
  @Test def testBearCallSpread() {
    val bearCallSpread = OptionPositionFactory.bearCallSpread("spy", 100, 105, Instant.now, 2, 1, 10)
    assert(bearCallSpread.maxGain == 1000, "maxGain was: " + bearCallSpread.maxGain)
    assert(bearCallSpread.maxLoss == -4000)
    
    // below lower strike
    assert(bearCallSpread.getValueAtExpiration(98) == 1000)
    // at lower strike
    assert(bearCallSpread.getValueAtExpiration(100) == 1000)
    // between strikes
    assert(bearCallSpread.getValueAtExpiration(103) == -2000)
    // at upper strike
    assert(bearCallSpread.getValueAtExpiration(105) == -4000)
    // above upper strike
    assert(bearCallSpread.getValueAtExpiration(108) == -4000)
    // break even
    assert(bearCallSpread.getValueAtExpiration(101) == 0)
  }
  
  @Test def testBullPutSpread() {
    val bullPutSpread = OptionPositionFactory.bullPutSpread("spy", 100, 105, Instant.now, 1, 2, 10)
    assert(bullPutSpread.maxGain == 4000, "maxGain was: " + bullPutSpread.maxGain)
    assert(bullPutSpread.maxLoss == -1000)
    
    // below lower strike
    assert(bullPutSpread.getValueAtExpiration(98) == 4000)
    // at lower strike
    assert(bullPutSpread.getValueAtExpiration(100) == 4000)
    // between strikes
    assert(bullPutSpread.getValueAtExpiration(103) == 1000)
    // at upper strike
    assert(bullPutSpread.getValueAtExpiration(105) == -1000)
    // above upper strike
    assert(bullPutSpread.getValueAtExpiration(108) == -1000)
    // break even
    assert(bullPutSpread.getValueAtExpiration(104) == 0)
  }
  
  @Test def testBearPutSpread() {
    val bearPutSpread = OptionPositionFactory.bearPutSpread("spy", 100, 105, Instant.now, 1, 2, 10)
    assert(bearPutSpread.maxGain == 1000, "maxGain was: " + bearPutSpread.maxGain)
    assert(bearPutSpread.maxLoss == -4000)
    
    // below lower strike
    assert(bearPutSpread.getValueAtExpiration(98) == -4000)
    // at lower strike
    assert(bearPutSpread.getValueAtExpiration(100) == -4000)
    // between strikes
    assert(bearPutSpread.getValueAtExpiration(103) == -1000)
    // at upper strike
    assert(bearPutSpread.getValueAtExpiration(105) == 1000)
    // above upper strike
    assert(bearPutSpread.getValueAtExpiration(108) == 1000)
    // break even
    assert(bearPutSpread.getValueAtExpiration(104) == 0)
  }
  
  @Test def testLongStraddle() {
    val straddle = OptionPositionFactory.longStraddle("spy", 100, Instant.now, 2, 2, 10)
    assert(straddle.maxGain == Double.PositiveInfinity)
    assert(straddle.maxLoss == -4000)
    
    // profitable below
    assert(straddle.getValueAtExpiration(90) == 6000)
    // break even below
    assert(straddle.getValueAtExpiration(96) == 0)
    // loss below
    assert(straddle.getValueAtExpiration(98) == -2000)
    // at strike
    assert(straddle.getValueAtExpiration(100) == -4000)
    // loss above
    assert(straddle.getValueAtExpiration(102) == -2000)
    // break even above
    assert(straddle.getValueAtExpiration(104) == 0)
    // profitable above
    assert(straddle.getValueAtExpiration(110) == 6000)
  }
  
  @Test def testShortStraddle() {
    val straddle = OptionPositionFactory.shortStraddle("spy", 100, Instant.now, 2, 2, 10)
    assert(straddle.maxGain == 4000)
    assert(straddle.maxLoss == Double.NegativeInfinity)
    
    // profitable below
    assert(straddle.getValueAtExpiration(90) == -6000)
    // break even below
    assert(straddle.getValueAtExpiration(96) == 0)
    // loss below
    assert(straddle.getValueAtExpiration(98) == 2000)
    // at strike
    assert(straddle.getValueAtExpiration(100) == 4000)
    // loss above
    assert(straddle.getValueAtExpiration(102) == 2000)
    // break even above
    assert(straddle.getValueAtExpiration(104) == 0)
    // profitable above
    assert(straddle.getValueAtExpiration(110) == -6000)
  }
  
  @Test def testLongStrangle() {
    val strangle = OptionPositionFactory.longStrangle("spy", 105, 95, Instant.now, 2, 2, 10)
    assert(strangle.maxGain == Double.PositiveInfinity)
    assert(strangle.maxLoss == -4000)
    
    // profitable below
    assert(strangle.getValueAtExpiration(90) == 1000)
    // break even below
    assert(strangle.getValueAtExpiration(91) == 0)
    // loss below 
    assert(strangle.getValueAtExpiration(94) == -3000)
    // at lower strike
    assert(strangle.getValueAtExpiration(95) == -4000)
    // in between strikes
    assert(strangle.getValueAtExpiration(100) == -4000)
    // at upper strike
    assert(strangle.getValueAtExpiration(105) == -4000)
    // loss above
    assert(strangle.getValueAtExpiration(106) == -3000)
    // break even above
    assert(strangle.getValueAtExpiration(109) == 0)
    // profitable above
    assert(strangle.getValueAtExpiration(110) == 1000)
  }
  
  @Test def testShortStrangle() {
    val strangle = OptionPositionFactory.shortStrangle("spy", 105, 95, Instant.now, 2, 2, 10)
    assert(strangle.maxGain == 4000)
    assert(strangle.maxLoss == Double.NegativeInfinity)
    
    // profitable below
    assert(strangle.getValueAtExpiration(90) == -1000)
    // break even below
    assert(strangle.getValueAtExpiration(91) == 0)
    // loss below 
    assert(strangle.getValueAtExpiration(94) == 3000)
    // at lower strike
    assert(strangle.getValueAtExpiration(95) == 4000)
    // in between strikes
    assert(strangle.getValueAtExpiration(100) == 4000)
    // at upper strike
    assert(strangle.getValueAtExpiration(105) == 4000)
    // loss above
    assert(strangle.getValueAtExpiration(106) == 3000)
    // break even above
    assert(strangle.getValueAtExpiration(109) == 0)
    // profitable above
    assert(strangle.getValueAtExpiration(110) == -1000)
  }
  
  @Test def testLongCallButterfly() {
    val butterfly = OptionPositionFactory.longCallButterfly("spy", 95, 100, 105, Instant.now, 1, 2, 1, 10)
    assert(butterfly.maxGain == 2000)
    assert(butterfly.maxLoss == -3000)
    
    // below lower strike
    assert(butterfly.getValueAtExpiration(90) == -3000)
    // at lower strike
    assert(butterfly.getValueAtExpiration(95) == -3000)
    // between lower and middle
    assert(butterfly.getValueAtExpiration(97) == -1000)
    // break even lower
    assert(butterfly.getValueAtExpiration(98) == 0)
    // profitable lower
    assert(butterfly.getValueAtExpiration(99) == 1000)
    // at middle
    assert(butterfly.getValueAtExpiration(100) == 2000)
    // profitable upper
    assert(butterfly.getValueAtExpiration(101) == 1000)
    // break even upper
    assert(butterfly.getValueAtExpiration(102) == 0)
    // between middle and upper
    assert(butterfly.getValueAtExpiration(103) == -1000)
    // at upper
    assert(butterfly.getValueAtExpiration(105) == -3000)
    // above upper
    assert(butterfly.getValueAtExpiration(110) == -3000)
  }
  
  @Test def testShortCallButterfly() {
    
  }
  
  @Test def testLongPutButterfly() {
    
  }
  
  @Test def testShortPutButterfly() {
    
  }
  
  @Test def testLongIronButterfly() {
    
  }
  
  @Test def testShortIronButterfly() {
    
  }
  
  @Test def testLongCallCondor() {
    
  }
  
  @Test def testShortCallCondor() {
    
  }
  
  @Test def testLongPutCondor() {
    
  }
  
  @Test def testShortPutCondor() {
    
  }
  
  @Test def testLongIronCondor() {
    
  }
  
  @Test def testShortIronCondor() {
    
  }
  
  @Test def testLongCallRatioBackspread() {
    
  }
  
  @Test def testShortCallRatioBackspread() {
    
  }
  
  @Test def testLongPutRatioBackspread() {
    
  }
  
  @Test def testShortPutRatioBackspread() {
    
  }
  
  @Test def testCoveredCall() {
    
  }
  
  @Test def testCoveredPut() {
    
  }
  
  @Test def testLongFence() {
    
  }
  
  @Test def testShortFence() {
    
  }
  
  
  
}