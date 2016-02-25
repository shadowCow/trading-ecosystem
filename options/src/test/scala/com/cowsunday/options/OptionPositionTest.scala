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
    assert(longCall.getNetGainAtExpiration(105) == 3000)
    // atm
    assert(longCall.getNetGainAtExpiration(100) == -2000)
    // otm
    assert(longCall.getNetGainAtExpiration(95) == -2000)
    // breakeven
    assert(longCall.getNetGainAtExpiration(102) == 0)
  }
  
  @Test def testShortCall() {
    val shortCall = OptionPositionFactory.shortCall("spy", 100, Instant.now, 2, 10)
    assert(shortCall.maxGain == 2000)
    assert(shortCall.maxLoss == Double.NegativeInfinity)
    
    // itm
    assert(shortCall.getNetGainAtExpiration(105) == -3000)
    // atm 
    assert(shortCall.getNetGainAtExpiration(100) == 2000)
    // otm
    assert(shortCall.getNetGainAtExpiration(95) == 2000)
    // breakeven
    assert(shortCall.getNetGainAtExpiration(102) == 0)
  }
  
  @Test def testLongPut() {
    val longPut = OptionPositionFactory.longPut("spy", 100, Instant.now, 2, 10)
    assert(longPut.maxGain == 98000)
    assert(longPut.maxLoss == -2000)
    
    // itm
    assert(longPut.getNetGainAtExpiration(95) == 3000)
    // atm
    assert(longPut.getNetGainAtExpiration(100) == -2000)
    // otm
    assert(longPut.getNetGainAtExpiration(105) == -2000)
    // breakeven
    assert(longPut.getNetGainAtExpiration(98) == 0)
  }
  
  @Test def testShortPut() {
    val shortPut = OptionPositionFactory.shortPut("spy", 100, Instant.now, 2, 10)
    assert(shortPut.maxGain == 2000)
    assert(shortPut.maxLoss == -98000)
    
    // itm
    assert(shortPut.getNetGainAtExpiration(95) == -3000)
    // atm
    assert(shortPut.getNetGainAtExpiration(100) == 2000)
    // otm
    assert(shortPut.getNetGainAtExpiration(105) == 2000)
    // breakeven
    assert(shortPut.getNetGainAtExpiration(98) == 0)
  }
  
  @Test def testBullCallSpread() {
    val bullCallSpread = OptionPositionFactory.bullCallSpread("spy", 100, 105, Instant.now, 2, 1, 10)
    assert(bullCallSpread.maxGain == 4000, "maxGain was: " + bullCallSpread.maxGain)
    assert(bullCallSpread.maxLoss == -1000)
    
    // below lower strike
    assert(bullCallSpread.getNetGainAtExpiration(98) == -1000)
    // at lower strike
    assert(bullCallSpread.getNetGainAtExpiration(100) == -1000)
    // between strikes
    assert(bullCallSpread.getNetGainAtExpiration(103) == 2000)
    // at upper strike
    assert(bullCallSpread.getNetGainAtExpiration(105) == 4000)
    // above upper strike
    assert(bullCallSpread.getNetGainAtExpiration(108) == 4000)
    // break even
    assert(bullCallSpread.getNetGainAtExpiration(101) == 0)
  }
  
  @Test def testBearCallSpread() {
    val bearCallSpread = OptionPositionFactory.bearCallSpread("spy", 100, 105, Instant.now, 2, 1, 10)
    assert(bearCallSpread.maxGain == 1000, "maxGain was: " + bearCallSpread.maxGain)
    assert(bearCallSpread.maxLoss == -4000)
    
    // below lower strike
    assert(bearCallSpread.getNetGainAtExpiration(98) == 1000)
    // at lower strike
    assert(bearCallSpread.getNetGainAtExpiration(100) == 1000)
    // between strikes
    assert(bearCallSpread.getNetGainAtExpiration(103) == -2000)
    // at upper strike
    assert(bearCallSpread.getNetGainAtExpiration(105) == -4000)
    // above upper strike
    assert(bearCallSpread.getNetGainAtExpiration(108) == -4000)
    // break even
    assert(bearCallSpread.getNetGainAtExpiration(101) == 0)
  }
  
  @Test def testBullPutSpread() {
    val bullPutSpread = OptionPositionFactory.bullPutSpread("spy", 100, 105, Instant.now, 1, 2, 10)
    assert(bullPutSpread.maxGain == 4000, "maxGain was: " + bullPutSpread.maxGain)
    assert(bullPutSpread.maxLoss == -1000)
    
    // below lower strike
    assert(bullPutSpread.getNetGainAtExpiration(98) == 4000)
    // at lower strike
    assert(bullPutSpread.getNetGainAtExpiration(100) == 4000)
    // between strikes
    assert(bullPutSpread.getNetGainAtExpiration(103) == 1000)
    // at upper strike
    assert(bullPutSpread.getNetGainAtExpiration(105) == -1000)
    // above upper strike
    assert(bullPutSpread.getNetGainAtExpiration(108) == -1000)
    // break even
    assert(bullPutSpread.getNetGainAtExpiration(104) == 0)
  }
  
  @Test def testBearPutSpread() {
    val bearPutSpread = OptionPositionFactory.bearPutSpread("spy", 100, 105, Instant.now, 1, 2, 10)
    assert(bearPutSpread.maxGain == 1000, "maxGain was: " + bearPutSpread.maxGain)
    assert(bearPutSpread.maxLoss == -4000)
    
    // below lower strike
    assert(bearPutSpread.getNetGainAtExpiration(98) == -4000)
    // at lower strike
    assert(bearPutSpread.getNetGainAtExpiration(100) == -4000)
    // between strikes
    assert(bearPutSpread.getNetGainAtExpiration(103) == -1000)
    // at upper strike
    assert(bearPutSpread.getNetGainAtExpiration(105) == 1000)
    // above upper strike
    assert(bearPutSpread.getNetGainAtExpiration(108) == 1000)
    // break even
    assert(bearPutSpread.getNetGainAtExpiration(104) == 0)
  }
  
  @Test def testLongStraddle() {
    val straddle = OptionPositionFactory.longStraddle("spy", 100, Instant.now, 2, 2, 10)
    assert(straddle.maxGain == Double.PositiveInfinity)
    assert(straddle.maxLoss == -4000)
    
    // profitable below
    assert(straddle.getNetGainAtExpiration(90) == 6000)
    // break even below
    assert(straddle.getNetGainAtExpiration(96) == 0)
    // loss below
    assert(straddle.getNetGainAtExpiration(98) == -2000)
    // at strike
    assert(straddle.getNetGainAtExpiration(100) == -4000)
    // loss above
    assert(straddle.getNetGainAtExpiration(102) == -2000)
    // break even above
    assert(straddle.getNetGainAtExpiration(104) == 0)
    // profitable above
    assert(straddle.getNetGainAtExpiration(110) == 6000)
  }
  
  @Test def testShortStraddle() {
    val straddle = OptionPositionFactory.shortStraddle("spy", 100, Instant.now, 2, 2, 10)
    assert(straddle.maxGain == 4000)
    assert(straddle.maxLoss == Double.NegativeInfinity, "actual max loss: " + straddle.maxLoss)
    
    // profitable below
    assert(straddle.getNetGainAtExpiration(90) == -6000)
    // break even below
    assert(straddle.getNetGainAtExpiration(96) == 0)
    // loss below
    assert(straddle.getNetGainAtExpiration(98) == 2000)
    // at strike
    assert(straddle.getNetGainAtExpiration(100) == 4000)
    // loss above
    assert(straddle.getNetGainAtExpiration(102) == 2000)
    // break even above
    assert(straddle.getNetGainAtExpiration(104) == 0)
    // profitable above
    assert(straddle.getNetGainAtExpiration(110) == -6000)
  }
  
  @Test def testLongStrangle() {
    val strangle = OptionPositionFactory.longStrangle("spy", 105, 95, Instant.now, 2, 2, 10)
    assert(strangle.maxGain == Double.PositiveInfinity)
    assert(strangle.maxLoss == -4000)
    
    // profitable below
    assert(strangle.getNetGainAtExpiration(90) == 1000)
    // break even below
    assert(strangle.getNetGainAtExpiration(91) == 0)
    // loss below 
    assert(strangle.getNetGainAtExpiration(94) == -3000)
    // at lower strike
    assert(strangle.getNetGainAtExpiration(95) == -4000)
    // in between strikes
    assert(strangle.getNetGainAtExpiration(100) == -4000)
    // at upper strike
    assert(strangle.getNetGainAtExpiration(105) == -4000)
    // loss above
    assert(strangle.getNetGainAtExpiration(106) == -3000)
    // break even above
    assert(strangle.getNetGainAtExpiration(109) == 0)
    // profitable above
    assert(strangle.getNetGainAtExpiration(110) == 1000)
  }
  
  @Test def testShortStrangle() {
    val strangle = OptionPositionFactory.shortStrangle("spy", 105, 95, Instant.now, 2, 2, 10)
    assert(strangle.maxGain == 4000)
    assert(strangle.maxLoss == Double.NegativeInfinity)
    
    // profitable below
    assert(strangle.getNetGainAtExpiration(90) == -1000)
    // break even below
    assert(strangle.getNetGainAtExpiration(91) == 0)
    // loss below 
    assert(strangle.getNetGainAtExpiration(94) == 3000)
    // at lower strike
    assert(strangle.getNetGainAtExpiration(95) == 4000)
    // in between strikes
    assert(strangle.getNetGainAtExpiration(100) == 4000)
    // at upper strike
    assert(strangle.getNetGainAtExpiration(105) == 4000)
    // loss above
    assert(strangle.getNetGainAtExpiration(106) == 3000)
    // break even above
    assert(strangle.getNetGainAtExpiration(109) == 0)
    // profitable above
    assert(strangle.getNetGainAtExpiration(110) == -1000)
  }
  
  @Test def testLongCallButterfly() {
    val butterfly = OptionPositionFactory.longCallButterfly("spy", 95, 100, 105, Instant.now, 4, 2, 1, 10)
    assert(butterfly.maxGain == 4000, "max gain was: " + butterfly.maxGain)
    assert(butterfly.maxLoss == -1000)
    
    // below lower strike
    assert(butterfly.getNetGainAtExpiration(90) == -1000)
    // at lower strike
    assert(butterfly.getNetGainAtExpiration(95) == -1000)
    // between lower and middle
    assert(butterfly.getNetGainAtExpiration(95.5) == -500)
    // break even lower
    assert(butterfly.getNetGainAtExpiration(96) == 0)
    // profitable lower
    assert(butterfly.getNetGainAtExpiration(99) == 3000)
    // at middle
    assert(butterfly.getNetGainAtExpiration(100) == 4000)
    // profitable upper
    assert(butterfly.getNetGainAtExpiration(101) == 3000)
    // break even upper
    assert(butterfly.getNetGainAtExpiration(104) == 0)
    // between middle and upper
    assert(butterfly.getNetGainAtExpiration(104.5) == -500)
    // at upper
    assert(butterfly.getNetGainAtExpiration(105) == -1000)
    // above upper
    assert(butterfly.getNetGainAtExpiration(110) == -1000)
  }
  
  @Test def testShortCallButterfly() {
    val butterfly = OptionPositionFactory.shortCallButterfly("spy", 95, 100, 105, Instant.now, 4, 2, 1, 10)
    assert(butterfly.maxGain == 1000, "max gain was: " + butterfly.maxGain)
    assert(butterfly.maxLoss == -4000)
    
    // below lower strike
    assert(butterfly.getNetGainAtExpiration(90) == 1000)
    // at lower strike
    assert(butterfly.getNetGainAtExpiration(95) == 1000)
    // between lower and middle
    assert(butterfly.getNetGainAtExpiration(95.5) == 500)
    // break even lower
    assert(butterfly.getNetGainAtExpiration(96) == 0)
    // profitable lower
    assert(butterfly.getNetGainAtExpiration(99) == -3000)
    // at middle
    assert(butterfly.getNetGainAtExpiration(100) == -4000)
    // profitable upper
    assert(butterfly.getNetGainAtExpiration(101) == -3000)
    // break even upper
    assert(butterfly.getNetGainAtExpiration(104) == 0)
    // between middle and upper
    assert(butterfly.getNetGainAtExpiration(104.5) == 500)
    // at upper
    assert(butterfly.getNetGainAtExpiration(105) == 1000)
    // above upper
    assert(butterfly.getNetGainAtExpiration(110) == 1000)
  }
  
  @Test def testLongPutButterfly() {
    val butterfly = OptionPositionFactory.longPutButterfly("spy", 95, 100, 105, Instant.now, 1, 2, 4, 10)
    assert(butterfly.maxGain == 4000, "max gain was: " + butterfly.maxGain)
    assert(butterfly.maxLoss == -1000)
    
    // below lower strike
    assert(butterfly.getNetGainAtExpiration(90) == -1000)
    // at lower strike
    assert(butterfly.getNetGainAtExpiration(95) == -1000)
    // between lower and middle
    assert(butterfly.getNetGainAtExpiration(95.5) == -500)
    // break even lower
    assert(butterfly.getNetGainAtExpiration(96) == 0)
    // profitable lower
    assert(butterfly.getNetGainAtExpiration(99) == 3000)
    // at middle
    assert(butterfly.getNetGainAtExpiration(100) == 4000)
    // profitable upper
    assert(butterfly.getNetGainAtExpiration(101) == 3000)
    // break even upper
    assert(butterfly.getNetGainAtExpiration(104) == 0)
    // between middle and upper
    assert(butterfly.getNetGainAtExpiration(104.5) == -500)
    // at upper
    assert(butterfly.getNetGainAtExpiration(105) == -1000)
    // above upper
    assert(butterfly.getNetGainAtExpiration(110) == -1000)
  }
  
  @Test def testShortPutButterfly() {
    val butterfly = OptionPositionFactory.shortPutButterfly("spy", 95, 100, 105, Instant.now, 1, 2, 4, 10)
    assert(butterfly.maxGain == 1000, "max gain was: " + butterfly.maxGain)
    assert(butterfly.maxLoss == -4000)
    
    // below lower strike
    assert(butterfly.getNetGainAtExpiration(90) == 1000)
    // at lower strike
    assert(butterfly.getNetGainAtExpiration(95) == 1000)
    // between lower and middle
    assert(butterfly.getNetGainAtExpiration(95.5) == 500)
    // break even lower
    assert(butterfly.getNetGainAtExpiration(96) == 0)
    // profitable lower
    assert(butterfly.getNetGainAtExpiration(99) == -3000)
    // at middle
    assert(butterfly.getNetGainAtExpiration(100) == -4000)
    // profitable upper
    assert(butterfly.getNetGainAtExpiration(101) == -3000)
    // break even upper
    assert(butterfly.getNetGainAtExpiration(104) == 0)
    // between middle and upper
    assert(butterfly.getNetGainAtExpiration(104.5) == 500)
    // at upper
    assert(butterfly.getNetGainAtExpiration(105) == 1000)
    // above upper
    assert(butterfly.getNetGainAtExpiration(110) == 1000)
  }
  
  @Test def testLongIronButterfly() {
    val butterfly = OptionPositionFactory.longIronButterfly("spy", 95, 100, 105, Instant.now, 1, 2, 1, 10)
    assert(butterfly.maxGain == 2000, "max gain was: " + butterfly.maxGain)
    assert(butterfly.maxLoss == -3000)
    
    // below lower strike
    assert(butterfly.getNetGainAtExpiration(90) == -3000)
    // at lower strike
    assert(butterfly.getNetGainAtExpiration(95) == -3000)
    // between lower and middle
    assert(butterfly.getNetGainAtExpiration(96) == -2000)
    // break even lower
    assert(butterfly.getNetGainAtExpiration(98) == 0)
    // profitable lower
    assert(butterfly.getNetGainAtExpiration(99) == 1000)
    // at middle
    assert(butterfly.getNetGainAtExpiration(100) == 2000)
    // profitable upper
    assert(butterfly.getNetGainAtExpiration(101) == 1000)
    // break even upper
    assert(butterfly.getNetGainAtExpiration(102) == 0)
    // between middle and upper
    assert(butterfly.getNetGainAtExpiration(104) == -2000)
    // at upper
    assert(butterfly.getNetGainAtExpiration(105) == -3000)
    // above upper
    assert(butterfly.getNetGainAtExpiration(110) == -3000)
  }
  
  @Test def testShortIronButterfly() {
    val butterfly = OptionPositionFactory.shortIronButterfly("spy", 95, 100, 105, Instant.now, 1, 2, 1, 10)
    assert(butterfly.maxGain == 3000, "max gain was: " + butterfly.maxGain)
    assert(butterfly.maxLoss == -2000)
    
    // below lower strike
    assert(butterfly.getNetGainAtExpiration(90) == 3000)
    // at lower strike
    assert(butterfly.getNetGainAtExpiration(95) == 3000)
    // between lower and middle
    assert(butterfly.getNetGainAtExpiration(96) == 2000)
    // break even lower
    assert(butterfly.getNetGainAtExpiration(98) == 0)
    // profitable lower
    assert(butterfly.getNetGainAtExpiration(99) == -1000)
    // at middle
    assert(butterfly.getNetGainAtExpiration(100) == -2000)
    // profitable upper
    assert(butterfly.getNetGainAtExpiration(101) == -1000)
    // break even upper
    assert(butterfly.getNetGainAtExpiration(102) == 0)
    // between middle and upper
    assert(butterfly.getNetGainAtExpiration(104) == 2000)
    // at upper
    assert(butterfly.getNetGainAtExpiration(105) == 3000)
    // above upper
    assert(butterfly.getNetGainAtExpiration(110) == 3000)
  }
  
  @Test def testLongCallCondor() {
    val condor = OptionPositionFactory.longCallCondor("spy", 90, 95, 100, 105, Instant.now, 7, 4, 3, 1, 10)
    assert(condor.maxGain == 4000, "max gain was: " + condor.maxGain)
    assert(condor.maxLoss == -1000)
    
    // below lower strike
    assert(condor.getNetGainAtExpiration(85) == -1000)
    // at lower strike
    assert(condor.getNetGainAtExpiration(90) == -1000)
    // between lower strikes
    assert(condor.getNetGainAtExpiration(90.5) == -500)
    // break even lower
    assert(condor.getNetGainAtExpiration(91) == 0)
    // between lower strikes profitable
    assert(condor.getNetGainAtExpiration(92) == 1000)
    // at lower middle strike
    assert(condor.getNetGainAtExpiration(95) == 4000)
    // between middle strikes
    assert(condor.getNetGainAtExpiration(97) == 4000)
    // at upper middle strike
    assert(condor.getNetGainAtExpiration(100) == 4000)
    // between upper strikes profitable
    assert(condor.getNetGainAtExpiration(103) == 1000)
    // break even upper
    assert(condor.getNetGainAtExpiration(104) == 0)
    // between upper strikes
    assert(condor.getNetGainAtExpiration(104.5) == -500)
    // at upper strike
    assert(condor.getNetGainAtExpiration(105) == -1000)
    // above upper strike
    assert(condor.getNetGainAtExpiration(110) == -1000)
  }
  
  @Test def testShortCallCondor() {
    val condor = OptionPositionFactory.shortCallCondor("spy", 90, 95, 100, 105, Instant.now, 7, 4, 3, 1, 10)
    assert(condor.maxGain == 1000, "max gain was: " + condor.maxGain)
    assert(condor.maxLoss == -4000)
    
    // below lower strike
    assert(condor.getNetGainAtExpiration(85) == 1000)
    // at lower strike
    assert(condor.getNetGainAtExpiration(90) == 1000)
    // between lower strikes
    assert(condor.getNetGainAtExpiration(90.5) == 500)
    // break even lower
    assert(condor.getNetGainAtExpiration(91) == 0)
    // between lower strikes profitable
    assert(condor.getNetGainAtExpiration(92) == -1000)
    // at lower middle strike
    assert(condor.getNetGainAtExpiration(95) == -4000)
    // between middle strikes
    assert(condor.getNetGainAtExpiration(97) == -4000)
    // at upper middle strike
    assert(condor.getNetGainAtExpiration(100) == -4000)
    // between upper strikes profitable
    assert(condor.getNetGainAtExpiration(103) == -1000)
    // break even upper
    assert(condor.getNetGainAtExpiration(104) == 0)
    // between upper strikes
    assert(condor.getNetGainAtExpiration(104.5) == 500)
    // at upper strike
    assert(condor.getNetGainAtExpiration(105) == 1000)
    // above upper strike
    assert(condor.getNetGainAtExpiration(110) == 1000)
  }
  
  @Test def testLongPutCondor() {
    val condor = OptionPositionFactory.longPutCondor("spy", 90, 95, 100, 105, Instant.now, 1, 3, 4, 7, 10)
    assert(condor.maxGain == 4000, "max gain was: " + condor.maxGain)
    assert(condor.maxLoss == -1000)
    
    // below lower strike
    assert(condor.getNetGainAtExpiration(85) == -1000)
    // at lower strike
    assert(condor.getNetGainAtExpiration(90) == -1000)
    // between lower strikes
    assert(condor.getNetGainAtExpiration(90.5) == -500)
    // break even lower
    assert(condor.getNetGainAtExpiration(91) == 0)
    // between lower strikes profitable
    assert(condor.getNetGainAtExpiration(92) == 1000)
    // at lower middle strike
    assert(condor.getNetGainAtExpiration(95) == 4000)
    // between middle strikes
    assert(condor.getNetGainAtExpiration(97) == 4000)
    // at upper middle strike
    assert(condor.getNetGainAtExpiration(100) == 4000)
    // between upper strikes profitable
    assert(condor.getNetGainAtExpiration(103) == 1000)
    // break even upper
    assert(condor.getNetGainAtExpiration(104) == 0)
    // between upper strikes
    assert(condor.getNetGainAtExpiration(104.5) == -500)
    // at upper strike
    assert(condor.getNetGainAtExpiration(105) == -1000)
    // above upper strike
    assert(condor.getNetGainAtExpiration(110) == -1000)
  }
  
  @Test def testShortPutCondor() {
    val condor = OptionPositionFactory.shortPutCondor("spy", 90, 95, 100, 105, Instant.now, 1, 3, 4, 7, 10)
    assert(condor.maxGain == 1000, "max gain was: " + condor.maxGain)
    assert(condor.maxLoss == -4000)
    
    // below lower strike
    assert(condor.getNetGainAtExpiration(85) == 1000)
    // at lower strike
    assert(condor.getNetGainAtExpiration(90) == 1000)
    // between lower strikes
    assert(condor.getNetGainAtExpiration(90.5) == 500)
    // break even lower
    assert(condor.getNetGainAtExpiration(91) == 0)
    // between lower strikes profitable
    assert(condor.getNetGainAtExpiration(92) == -1000)
    // at lower middle strike
    assert(condor.getNetGainAtExpiration(95) == -4000)
    // between middle strikes
    assert(condor.getNetGainAtExpiration(97) == -4000)
    // at upper middle strike
    assert(condor.getNetGainAtExpiration(100) == -4000)
    // between upper strikes profitable
    assert(condor.getNetGainAtExpiration(103) == -1000)
    // break even upper
    assert(condor.getNetGainAtExpiration(104) == 0)
    // between upper strikes
    assert(condor.getNetGainAtExpiration(104.5) == 500)
    // at upper strike
    assert(condor.getNetGainAtExpiration(105) == 1000)
    // above upper strike
    assert(condor.getNetGainAtExpiration(110) == 1000)
  }
  
  @Test def testLongIronCondor() {
    val condor = OptionPositionFactory.longIronCondor("spy", 90, 95, 100, 105, Instant.now, 1, 3, 3, 1, 10)
    assert(condor.maxGain == 4000, "max gain was: " + condor.maxGain)
    //assert(condor.maxLoss == -1000, "max loss was: " + condor.maxLoss)
    
    // below lower strike
    assert(condor.getNetGainAtExpiration(85) == -1000)
    // at lower strike
    assert(condor.getNetGainAtExpiration(90) == -1000)
    // between lower strikes
    assert(condor.getNetGainAtExpiration(90.5) == -500)
    // break even lower
    assert(condor.getNetGainAtExpiration(91) == 0)
    // between lower strikes profitable
    assert(condor.getNetGainAtExpiration(92) == 1000)
    // at lower middle strike
    assert(condor.getNetGainAtExpiration(95) == 4000)
    // between middle strikes
    assert(condor.getNetGainAtExpiration(97) == 4000)
    // at upper middle strike
    assert(condor.getNetGainAtExpiration(100) == 4000)
    // between upper strikes profitable
    assert(condor.getNetGainAtExpiration(103) == 1000)
    // break even upper
    assert(condor.getNetGainAtExpiration(104) == 0)
    // between upper strikes
    assert(condor.getNetGainAtExpiration(104.5) == -500)
    // at upper strike
    assert(condor.getNetGainAtExpiration(105) == -1000)
    // above upper strike
    assert(condor.getNetGainAtExpiration(110) == -1000)
    // infinity
    assert(condor.getNetGainAtExpiration(Double.PositiveInfinity) == -1000, "value at infinity was: " + condor.getNetGainAtExpiration(Double.PositiveInfinity))
  }
  
  @Test def testShortIronCondor() {
    val condor = OptionPositionFactory.shortIronCondor("spy", 90, 95, 100, 105, Instant.now, 1, 3, 3, 1, 10)
    assert(condor.maxGain == 1000, "max gain was: " + condor.maxGain)
    assert(condor.maxLoss == -4000)
    
    // below lower strike
    assert(condor.getNetGainAtExpiration(85) == 1000)
    // at lower strike
    assert(condor.getNetGainAtExpiration(90) == 1000)
    // between lower strikes
    assert(condor.getNetGainAtExpiration(90.5) == 500)
    // break even lower
    assert(condor.getNetGainAtExpiration(91) == 0)
    // between lower strikes profitable
    assert(condor.getNetGainAtExpiration(92) == -1000)
    // at lower middle strike
    assert(condor.getNetGainAtExpiration(95) == -4000)
    // between middle strikes
    assert(condor.getNetGainAtExpiration(97) == -4000)
    // at upper middle strike
    assert(condor.getNetGainAtExpiration(100) == -4000)
    // between upper strikes profitable
    assert(condor.getNetGainAtExpiration(103) == -1000)
    // break even upper
    assert(condor.getNetGainAtExpiration(104) == 0)
    // between upper strikes
    assert(condor.getNetGainAtExpiration(104.5) == 500)
    // at upper strike
    assert(condor.getNetGainAtExpiration(105) == 1000)
    // above upper strike
    assert(condor.getNetGainAtExpiration(110) == 1000)
  }
  
  @Test def testLongCallRatioBackspread() {
    val backspread = OptionPositionFactory.longCallRatioBackspread("spy", 100, 105, Instant.now, 3, 1, 10, 20)
    assert(backspread.maxGain == Double.PositiveInfinity)
    assert(backspread.maxLoss == -4000)
    
    // below lower
    assert(backspread.getNetGainAtExpiration(95) == 1000)
    // at lower
    assert(backspread.getNetGainAtExpiration(100) == 1000)
    // break even lower
    assert(backspread.getNetGainAtExpiration(101) == 0)
    // between strikes
    assert(backspread.getNetGainAtExpiration(103) == -2000)
    // at upper
    assert(backspread.getNetGainAtExpiration(105) == -4000)
    // above upper
    assert(backspread.getNetGainAtExpiration(106) == -3000)
    // break even upper
    assert(backspread.getNetGainAtExpiration(109) == 0)
    // above upper break even profitable
    assert(backspread.getNetGainAtExpiration(110) == 1000)
  }
  
  @Test def testShortCallRatioBackspread() {
    val backspread = OptionPositionFactory.shortCallRatioBackspread("spy", 100, 105, Instant.now, 3, 1, 10, 20)
    assert(backspread.maxGain == 4000)
    assert(backspread.maxLoss == Double.NegativeInfinity)
    
    // below lower
    assert(backspread.getNetGainAtExpiration(95) == -1000)
    // at lower
    assert(backspread.getNetGainAtExpiration(100) == -1000)
    // break even lower
    assert(backspread.getNetGainAtExpiration(101) == 0)
    // between strikes
    assert(backspread.getNetGainAtExpiration(103) == 2000)
    // at upper
    assert(backspread.getNetGainAtExpiration(105) == 4000)
    // above upper
    assert(backspread.getNetGainAtExpiration(106) == 3000)
    // break even upper
    assert(backspread.getNetGainAtExpiration(109) == 0)
    // above upper break even profitable
    assert(backspread.getNetGainAtExpiration(110) == -1000)
  }
  
  @Test def testLongPutRatioBackspread() {
    val backspread = OptionPositionFactory.longPutRatioBackspread("spy", 100, 105, Instant.now, 1, 3, 20, 10)
    assert(backspread.maxGain == (99*2 - 102) * 1000, "max gain was: " + backspread.maxGain)
    assert(backspread.maxLoss == -4000)
    
    // below lower profitable
    assert(backspread.getNetGainAtExpiration(95) == 1000)
    // break even lower
    assert(backspread.getNetGainAtExpiration(96) == 0)
    // below lower
    assert(backspread.getNetGainAtExpiration(99) == -3000)
    // at lower
    assert(backspread.getNetGainAtExpiration(100) == -4000)
    // between strikes
    assert(backspread.getNetGainAtExpiration(102) == -2000)
    // break even upper
    assert(backspread.getNetGainAtExpiration(104) == 0)
    // at upper
    assert(backspread.getNetGainAtExpiration(105) == 1000)
    // above upper
    assert(backspread.getNetGainAtExpiration(110) == 1000)
  }
  
  @Test def testShortPutRatioBackspread() {
    val backspread = OptionPositionFactory.shortPutRatioBackspread("spy", 100, 105, Instant.now, 1, 3, 20, 10)
    assert(backspread.maxGain == 4000, "max gain was: " + backspread.maxGain)
    assert(backspread.maxLoss == -(99*2 - 102) * 1000)
    
    // below lower profitable
    assert(backspread.getNetGainAtExpiration(95) == -1000)
    // break even lower
    assert(backspread.getNetGainAtExpiration(96) == 0)
    // below lower
    assert(backspread.getNetGainAtExpiration(99) == 3000)
    // at lower
    assert(backspread.getNetGainAtExpiration(100) == 4000)
    // between strikes
    assert(backspread.getNetGainAtExpiration(102) == 2000)
    // break even upper
    assert(backspread.getNetGainAtExpiration(104) == 0)
    // at upper
    assert(backspread.getNetGainAtExpiration(105) == -1000)
    // above upper
    assert(backspread.getNetGainAtExpiration(110) == -1000)
  }
  
  @Test def testCoveredCall() {
    val covered = OptionPositionFactory.coveredCall("spy", 100, Instant.now, 3, 100, 10)
    assert(covered.maxGain == 3000)
    assert(covered.maxLoss == -97000)
    
    // at 0
    assert(covered.getNetGainAtExpiration(0) == -97000)
    // below strike
    assert(covered.getNetGainAtExpiration(95) == -2000)
    // break even
    assert(covered.getNetGainAtExpiration(97) == 0)
    // below strike profitable
    assert(covered.getNetGainAtExpiration(99) == 2000)
    // at strike
    assert(covered.getNetGainAtExpiration(100) == 3000)
    // above strike
    assert(covered.getNetGainAtExpiration(105) == 3000)
    // at infinity
    // stock position is not being offset against the options - fix that
    assert(covered.getNetGainAtExpiration(Double.PositiveInfinity) == 3000)
  }
  
  @Test def testCoveredPut() {
    
  }
  
  @Test def testLongFence() {
    
  }
  
  @Test def testShortFence() {
    
  }
  
  
  
}