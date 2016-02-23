package com.cowsunday.options

import java.time._

object OptionPositionFactory {

  def longCall(symbol: String, strikePrice: Double, expirationDate: Instant, entryPrice: Double, quantity: Int): OptionPosition = {
    val option = new Option(symbol, OptionType.CALL, strikePrice, expirationDate)
    val leg = new OptionLeg(option, entryPrice, quantity, PositionDirection.LONG)
    
    new OptionPosition(List(leg))
  }
  
  def shortCall(symbol: String, strikePrice: Double, expirationDate: Instant, entryPrice: Double, quantity: Int): OptionPosition = {
    val option = new Option(symbol, OptionType.CALL, strikePrice, expirationDate)
    val leg = new OptionLeg(option, entryPrice, quantity, PositionDirection.SHORT)
    
    new OptionPosition(List(leg))
  }
  
  def longPut(symbol: String, strikePrice: Double, expirationDate: Instant, entryPrice: Double, quantity: Int): OptionPosition = {
    val option = new Option(symbol, OptionType.PUT, strikePrice, expirationDate)
    val leg = new OptionLeg(option, entryPrice, quantity, PositionDirection.LONG)
    
    new OptionPosition(List(leg))
  }
  
  def shortPut(symbol: String, strikePrice: Double, expirationDate: Instant, entryPrice: Double, quantity: Int): OptionPosition = {
    val option = new Option(symbol, OptionType.PUT, strikePrice, expirationDate)
    val leg = new OptionLeg(option, entryPrice, quantity, PositionDirection.SHORT)
    
    new OptionPosition(List(leg))
  }
  
  def bullCallSpread(symbol: String, strikePriceLower: Double, strikePriceUpper: Double, expirationDate: Instant, entryPriceLower: Double, entryPriceUpper: Double, quantity: Int): OptionPosition = {
    val optionLower = new Option(symbol, OptionType.CALL, strikePriceLower, expirationDate)
    val legLower = new OptionLeg(optionLower, entryPriceLower, quantity, PositionDirection.LONG)
    
    val optionUpper = new Option(symbol, OptionType.CALL, strikePriceUpper, expirationDate)
    val legUpper = new OptionLeg(optionUpper, entryPriceUpper, quantity, PositionDirection.SHORT)
    
    new OptionPosition(List(legLower, legUpper))
  }
  
  def bearCallSpread(symbol: String, strikePriceLower: Double, strikePriceUpper: Double, expirationDate: Instant, entryPriceLower: Double, entryPriceUpper: Double, quantity: Int): OptionPosition = {
    val optionLower = new Option(symbol, OptionType.CALL, strikePriceLower, expirationDate)
    val legLower = new OptionLeg(optionLower, entryPriceLower, quantity, PositionDirection.SHORT)
    
    val optionUpper = new Option(symbol, OptionType.CALL, strikePriceUpper, expirationDate)
    val legUpper = new OptionLeg(optionUpper, entryPriceUpper, quantity, PositionDirection.LONG)
    
    new OptionPosition(List(legLower, legUpper))
  }
  
  def bullPutSpread(symbol: String, strikePriceLower: Double, strikePriceUpper: Double, expirationDate: Instant, entryPriceLower: Double, entryPriceUpper: Double, quantity: Int): OptionPosition = {
    val optionLower = new Option(symbol, OptionType.PUT, strikePriceLower, expirationDate)
    val legLower = new OptionLeg(optionLower, entryPriceLower, quantity, PositionDirection.SHORT)
    
    val optionUpper = new Option(symbol, OptionType.PUT, strikePriceUpper, expirationDate)
    val legUpper = new OptionLeg(optionUpper, entryPriceUpper, quantity, PositionDirection.LONG)
    
    new OptionPosition(List(legLower, legUpper))
  }
  
  def bearPutSpread(symbol: String, strikePriceLower: Double, strikePriceUpper: Double, expirationDate: Instant, entryPriceLower: Double, entryPriceUpper: Double, quantity: Int): OptionPosition = {
    val optionLower = new Option(symbol, OptionType.PUT, strikePriceLower, expirationDate)
    val legLower = new OptionLeg(optionLower, entryPriceLower, quantity, PositionDirection.LONG)
    
    val optionUpper = new Option(symbol, OptionType.PUT, strikePriceUpper, expirationDate)
    val legUpper = new OptionLeg(optionUpper, entryPriceUpper, quantity, PositionDirection.SHORT)
    
    new OptionPosition(List(legLower, legUpper))
  }
  
  def longStraddle(symbol: String, strikePrice: Double, expirationDate: Instant, entryPriceCall: Double, entryPricePut: Double, quantity: Int): OptionPosition = {
    val call = new Option(symbol, OptionType.CALL, strikePrice, expirationDate)
    val legCall = new OptionLeg(call, entryPriceCall, quantity, PositionDirection.LONG)
    
    val put = new Option(symbol, OptionType.PUT, strikePrice, expirationDate)
    val legPut = new OptionLeg(put, entryPricePut, quantity, PositionDirection.LONG)
    
    new OptionPosition(List(legCall, legPut))
  }
  
  def shortStraddle(symbol: String, strikePrice: Double, expirationDate: Instant, entryPriceCall: Double, entryPricePut: Double, quantity: Int): OptionPosition = {
    val call = new Option(symbol, OptionType.CALL, strikePrice, expirationDate)
    val legCall = new OptionLeg(call, entryPriceCall, quantity, PositionDirection.SHORT)
    
    val put = new Option(symbol, OptionType.PUT, strikePrice, expirationDate)
    val legPut = new OptionLeg(put, entryPricePut, quantity, PositionDirection.SHORT)
    
    new OptionPosition(List(legCall, legPut))
  }
  
  def longStrangle(symbol: String, strikePriceCall: Double, strikePricePut: Double, expirationDate: Instant, entryPriceCall: Double, entryPricePut: Double, quantity: Int): OptionPosition = {
    val call = new Option(symbol, OptionType.CALL, strikePriceCall, expirationDate)
    val legCall = new OptionLeg(call, entryPriceCall, quantity, PositionDirection.LONG)
    
    val put = new Option(symbol, OptionType.PUT, strikePricePut, expirationDate)
    val legPut = new OptionLeg(put, entryPricePut, quantity, PositionDirection.LONG)
    
    new OptionPosition(List(legCall, legPut))
  }
  
  def shortStrangle(symbol: String, strikePriceCall: Double, strikePricePut: Double, expirationDate: Instant, entryPriceCall: Double, entryPricePut: Double, quantity: Int): OptionPosition = {
    val call = new Option(symbol, OptionType.CALL, strikePriceCall, expirationDate)
    val legCall = new OptionLeg(call, entryPriceCall, quantity, PositionDirection.SHORT)
    
    val put = new Option(symbol, OptionType.PUT, strikePricePut, expirationDate)
    val legPut = new OptionLeg(put, entryPricePut, quantity, PositionDirection.SHORT)
    
    new OptionPosition(List(legCall, legPut))
  }
  
  def longCallButterfly(symbol: String, strikePriceLower: Double, strikePriceMiddle: Double, strikePriceUpper: Double, expirationDate: Instant, entryPriceLower: Double, entryPriceMiddle: Double, entryPriceUpper: Double, quantity: Int): OptionPosition = {
    val lower = new Option(symbol, OptionType.CALL, strikePriceLower, expirationDate)
    val lowerLeg = new OptionLeg(lower, entryPriceLower, quantity, PositionDirection.LONG)
    
    val middle = new Option(symbol, OptionType.CALL, strikePriceMiddle, expirationDate)
    // double the quantity instead of doing 2 middle legs
    val middleLeg = new OptionLeg(middle, entryPriceMiddle, quantity*2, PositionDirection.SHORT)
    
    val upper = new Option(symbol, OptionType.CALL, strikePriceUpper, expirationDate)
    val upperLeg = new OptionLeg(upper, entryPriceUpper, quantity, PositionDirection.LONG)
    
    new OptionPosition(List(lowerLeg, middleLeg, upperLeg))
  }
  
  def shortCallButterfly(symbol: String, strikePriceLower: Double, strikePriceMiddle: Double, strikePriceUpper: Double, expirationDate: Instant, entryPriceLower: Double, entryPriceMiddle: Double, entryPriceUpper: Double, quantity: Int): OptionPosition = {
    val lower = new Option(symbol, OptionType.CALL, strikePriceLower, expirationDate)
    val lowerLeg = new OptionLeg(lower, entryPriceLower, quantity, PositionDirection.SHORT)
    
    val middle = new Option(symbol, OptionType.CALL, strikePriceMiddle, expirationDate)
    // double the quantity instead of doing 2 middle legs
    val middleLeg = new OptionLeg(middle, entryPriceMiddle, quantity*2, PositionDirection.LONG)
    
    val upper = new Option(symbol, OptionType.CALL, strikePriceUpper, expirationDate)
    val upperLeg = new OptionLeg(upper, entryPriceUpper, quantity, PositionDirection.SHORT)
    
    new OptionPosition(List(lowerLeg, middleLeg, upperLeg))
  }
  
  def longPutButterfly(symbol: String, strikePriceLower: Double, strikePriceMiddle: Double, strikePriceUpper: Double, expirationDate: Instant, entryPriceLower: Double, entryPriceMiddle: Double, entryPriceUpper: Double, quantity: Int): OptionPosition = {
    val lower = new Option(symbol, OptionType.PUT, strikePriceLower, expirationDate)
    val lowerLeg = new OptionLeg(lower, entryPriceLower, quantity, PositionDirection.LONG)
    
    val middle = new Option(symbol, OptionType.PUT, strikePriceMiddle, expirationDate)
    // double the quantity instead of doing 2 middle legs
    val middleLeg = new OptionLeg(middle, entryPriceMiddle, quantity*2, PositionDirection.SHORT)
    
    val upper = new Option(symbol, OptionType.PUT, strikePriceUpper, expirationDate)
    val upperLeg = new OptionLeg(upper, entryPriceUpper, quantity, PositionDirection.LONG)
    
    new OptionPosition(List(lowerLeg, middleLeg, upperLeg))
  }
  
  def shortPutButterfly(symbol: String, strikePriceLower: Double, strikePriceMiddle: Double, strikePriceUpper: Double, expirationDate: Instant, entryPriceLower: Double, entryPriceMiddle: Double, entryPriceUpper: Double, quantity: Int): OptionPosition = {
    val lower = new Option(symbol, OptionType.PUT, strikePriceLower, expirationDate)
    val lowerLeg = new OptionLeg(lower, entryPriceLower, quantity, PositionDirection.SHORT)
    
    val middle = new Option(symbol, OptionType.PUT, strikePriceMiddle, expirationDate)
    // double the quantity instead of doing 2 middle legs
    val middleLeg = new OptionLeg(middle, entryPriceMiddle, quantity*2, PositionDirection.LONG)
    
    val upper = new Option(symbol, OptionType.PUT, strikePriceUpper, expirationDate)
    val upperLeg = new OptionLeg(upper, entryPriceUpper, quantity, PositionDirection.SHORT)
    
    new OptionPosition(List(lowerLeg, middleLeg, upperLeg))
  }
  
  def longIronButterfly(symbol: String, strikePriceLower: Double, strikePriceMiddle: Double, strikePriceUpper: Double, expirationDate: Instant, entryPriceLower: Double, entryPriceMiddle: Double, entryPriceUpper: Double, quantity: Int): OptionPosition = {
    val lower = new Option(symbol, OptionType.PUT, strikePriceLower, expirationDate)
    val lowerLeg = new OptionLeg(lower, entryPriceLower, quantity, PositionDirection.LONG)
    
    val middlePut = new Option(symbol, OptionType.PUT, strikePriceMiddle, expirationDate)
    val middlePutLeg = new OptionLeg(middlePut, entryPriceMiddle, quantity, PositionDirection.SHORT)
    
    val middleCall = new Option(symbol, OptionType.CALL, strikePriceMiddle, expirationDate)
    val middleCallLeg = new OptionLeg(middleCall, entryPriceMiddle, quantity, PositionDirection.SHORT)
    
    val upper = new Option(symbol, OptionType.CALL, strikePriceUpper, expirationDate)
    val upperLeg = new OptionLeg(upper, entryPriceUpper, quantity, PositionDirection.LONG)
    
    new OptionPosition(List(lowerLeg, middlePutLeg, middleCallLeg, upperLeg))
  }
  
  def shortIronButterfly(symbol: String, strikePriceLower: Double, strikePriceMiddle: Double, strikePriceUpper: Double, expirationDate: Instant, entryPriceLower: Double, entryPriceMiddle: Double, entryPriceUpper: Double, quantity: Int): OptionPosition = {
    val lower = new Option(symbol, OptionType.PUT, strikePriceLower, expirationDate)
    val lowerLeg = new OptionLeg(lower, entryPriceLower, quantity, PositionDirection.SHORT)
    
    val middlePut = new Option(symbol, OptionType.PUT, strikePriceMiddle, expirationDate)
    val middlePutLeg = new OptionLeg(middlePut, entryPriceMiddle, quantity, PositionDirection.LONG)
    
    val middleCall = new Option(symbol, OptionType.CALL, strikePriceMiddle, expirationDate)
    val middleCallLeg = new OptionLeg(middleCall, entryPriceMiddle, quantity, PositionDirection.LONG)
    
    val upper = new Option(symbol, OptionType.CALL, strikePriceUpper, expirationDate)
    val upperLeg = new OptionLeg(upper, entryPriceUpper, quantity, PositionDirection.SHORT)
    
    new OptionPosition(List(lowerLeg, middlePutLeg, middleCallLeg, upperLeg))
  }
  
  def longCallCondor(symbol: String, strikePriceLower: Double, strikePriceLowerMiddle: Double, strikePriceUpperMiddle: Double, strikePriceUpper: Double, expirationDate: Instant, entryPriceLower: Double, entryPriceLowerMiddle: Double, entryPriceUpperMiddle: Double, entryPriceUpper: Double, quantity: Int): OptionPosition = {
    val lower = new Option(symbol, OptionType.CALL, strikePriceLower, expirationDate)
    val lowerLeg = new OptionLeg(lower, entryPriceLower, quantity, PositionDirection.LONG)
    
    val middlePut = new Option(symbol, OptionType.CALL, strikePriceLowerMiddle, expirationDate)
    val middlePutLeg = new OptionLeg(middlePut, entryPriceLowerMiddle, quantity, PositionDirection.SHORT)
    
    val middleCall = new Option(symbol, OptionType.CALL, strikePriceUpperMiddle, expirationDate)
    val middleCallLeg = new OptionLeg(middleCall, entryPriceUpperMiddle, quantity, PositionDirection.SHORT)
    
    val upper = new Option(symbol, OptionType.CALL, strikePriceUpper, expirationDate)
    val upperLeg = new OptionLeg(upper, entryPriceUpper, quantity, PositionDirection.LONG)
    
    new OptionPosition(List(lowerLeg, middlePutLeg, middleCallLeg, upperLeg))
  }
  
  def shortCallCondor(symbol: String, strikePriceLower: Double, strikePriceLowerMiddle: Double, strikePriceUpperMiddle: Double, strikePriceUpper: Double, expirationDate: Instant, entryPriceLower: Double, entryPriceLowerMiddle: Double, entryPriceUpperMiddle: Double, entryPriceUpper: Double, quantity: Int): OptionPosition = {
    val lower = new Option(symbol, OptionType.CALL, strikePriceLower, expirationDate)
    val lowerLeg = new OptionLeg(lower, entryPriceLower, quantity, PositionDirection.SHORT)
    
    val middlePut = new Option(symbol, OptionType.CALL, strikePriceLowerMiddle, expirationDate)
    val middlePutLeg = new OptionLeg(middlePut, entryPriceLowerMiddle, quantity, PositionDirection.LONG)
    
    val middleCall = new Option(symbol, OptionType.CALL, strikePriceUpperMiddle, expirationDate)
    val middleCallLeg = new OptionLeg(middleCall, entryPriceUpperMiddle, quantity, PositionDirection.LONG)
    
    val upper = new Option(symbol, OptionType.CALL, strikePriceUpper, expirationDate)
    val upperLeg = new OptionLeg(upper, entryPriceUpper, quantity, PositionDirection.SHORT)
    
    new OptionPosition(List(lowerLeg, middlePutLeg, middleCallLeg, upperLeg))
  }
  
  def longPutCondor(symbol: String, strikePriceLower: Double, strikePriceLowerMiddle: Double, strikePriceUpperMiddle: Double, strikePriceUpper: Double, expirationDate: Instant, entryPriceLower: Double, entryPriceLowerMiddle: Double, entryPriceUpperMiddle: Double, entryPriceUpper: Double, quantity: Int): OptionPosition = {
    val lower = new Option(symbol, OptionType.PUT, strikePriceLower, expirationDate)
    val lowerLeg = new OptionLeg(lower, entryPriceLower, quantity, PositionDirection.LONG)
    
    val middlePut = new Option(symbol, OptionType.PUT, strikePriceLowerMiddle, expirationDate)
    val middlePutLeg = new OptionLeg(middlePut, entryPriceLowerMiddle, quantity, PositionDirection.SHORT)
    
    val middleCall = new Option(symbol, OptionType.PUT, strikePriceUpperMiddle, expirationDate)
    val middleCallLeg = new OptionLeg(middleCall, entryPriceUpperMiddle, quantity, PositionDirection.SHORT)
    
    val upper = new Option(symbol, OptionType.PUT, strikePriceUpper, expirationDate)
    val upperLeg = new OptionLeg(upper, entryPriceUpper, quantity, PositionDirection.LONG)
    
    new OptionPosition(List(lowerLeg, middlePutLeg, middleCallLeg, upperLeg))
  }
  
  def shortPutCondor(symbol: String, strikePriceLower: Double, strikePriceLowerMiddle: Double, strikePriceUpperMiddle: Double, strikePriceUpper: Double, expirationDate: Instant, entryPriceLower: Double, entryPriceLowerMiddle: Double, entryPriceUpperMiddle: Double, entryPriceUpper: Double, quantity: Int): OptionPosition = {
    val lower = new Option(symbol, OptionType.PUT, strikePriceLower, expirationDate)
    val lowerLeg = new OptionLeg(lower, entryPriceLower, quantity, PositionDirection.SHORT)
    
    val middlePut = new Option(symbol, OptionType.PUT, strikePriceLowerMiddle, expirationDate)
    val middlePutLeg = new OptionLeg(middlePut, entryPriceLowerMiddle, quantity, PositionDirection.LONG)
    
    val middleCall = new Option(symbol, OptionType.PUT, strikePriceUpperMiddle, expirationDate)
    val middleCallLeg = new OptionLeg(middleCall, entryPriceUpperMiddle, quantity, PositionDirection.LONG)
    
    val upper = new Option(symbol, OptionType.PUT, strikePriceUpper, expirationDate)
    val upperLeg = new OptionLeg(upper, entryPriceUpper, quantity, PositionDirection.SHORT)
    
    new OptionPosition(List(lowerLeg, middlePutLeg, middleCallLeg, upperLeg))
  }
  
  def longIronCondor(symbol: String, strikePriceLower: Double, strikePriceLowerMiddle: Double, strikePriceUpperMiddle: Double, strikePriceUpper: Double, expirationDate: Instant, entryPriceLower: Double, entryPriceLowerMiddle: Double, entryPriceUpperMiddle: Double, entryPriceUpper: Double, quantity: Int): OptionPosition = {
    val lower = new Option(symbol, OptionType.PUT, strikePriceLower, expirationDate)
    val lowerLeg = new OptionLeg(lower, entryPriceLower, quantity, PositionDirection.LONG)
    
    val middlePut = new Option(symbol, OptionType.PUT, strikePriceLowerMiddle, expirationDate)
    val middlePutLeg = new OptionLeg(middlePut, entryPriceLowerMiddle, quantity, PositionDirection.SHORT)
    
    val middleCall = new Option(symbol, OptionType.CALL, strikePriceUpperMiddle, expirationDate)
    val middleCallLeg = new OptionLeg(middleCall, entryPriceUpperMiddle, quantity, PositionDirection.SHORT)
    
    val upper = new Option(symbol, OptionType.CALL, strikePriceUpper, expirationDate)
    val upperLeg = new OptionLeg(upper, entryPriceUpper, quantity, PositionDirection.LONG)
    
    new OptionPosition(List(lowerLeg, middlePutLeg, middleCallLeg, upperLeg))
  }
  
  def shortIronCondor(symbol: String, strikePriceLower: Double, strikePriceLowerMiddle: Double, strikePriceUpperMiddle: Double, strikePriceUpper: Double, expirationDate: Instant, entryPriceLower: Double, entryPriceLowerMiddle: Double, entryPriceUpperMiddle: Double, entryPriceUpper: Double, quantity: Int): OptionPosition = {
    val lower = new Option(symbol, OptionType.PUT, strikePriceLower, expirationDate)
    val lowerLeg = new OptionLeg(lower, entryPriceLower, quantity, PositionDirection.SHORT)
    
    val middlePut = new Option(symbol, OptionType.PUT, strikePriceLowerMiddle, expirationDate)
    val middlePutLeg = new OptionLeg(middlePut, entryPriceLowerMiddle, quantity, PositionDirection.LONG)
    
    val middleCall = new Option(symbol, OptionType.CALL, strikePriceUpperMiddle, expirationDate)
    val middleCallLeg = new OptionLeg(middleCall, entryPriceUpperMiddle, quantity, PositionDirection.LONG)
    
    val upper = new Option(symbol, OptionType.CALL, strikePriceUpper, expirationDate)
    val upperLeg = new OptionLeg(upper, entryPriceUpper, quantity, PositionDirection.SHORT)
    
    new OptionPosition(List(lowerLeg, middlePutLeg, middleCallLeg, upperLeg))
  }
  
  def longCallRatioBackspread(symbol: String, strikePriceLower: Double, strikePriceUpper: Double, expirationDate: Instant, entryPriceLower: Double, entryPriceUpper: Double, quantityLower: Int, quantityUpper: Int): OptionPosition = {
    val lower = new Option(symbol, OptionType.CALL, strikePriceLower, expirationDate)
    val lowerLeg = new OptionLeg(lower, entryPriceLower, quantityLower, PositionDirection.SHORT)
    
    val upper = new Option(symbol, OptionType.CALL, strikePriceUpper, expirationDate)
    val upperLeg = new OptionLeg(upper, entryPriceUpper, quantityUpper, PositionDirection.LONG)
    
    new OptionPosition(List(lowerLeg, upperLeg))
  }
  
  def shortCallRatioBackspread(symbol: String, strikePriceLower: Double, strikePriceUpper: Double, expirationDate: Instant, entryPriceLower: Double, entryPriceUpper: Double, quantityLower: Int, quantityUpper: Int): OptionPosition = {
    val lower = new Option(symbol, OptionType.CALL, strikePriceLower, expirationDate)
    val lowerLeg = new OptionLeg(lower, entryPriceLower, quantityLower, PositionDirection.LONG)
    
    val upper = new Option(symbol, OptionType.CALL, strikePriceUpper, expirationDate)
    val upperLeg = new OptionLeg(upper, entryPriceUpper, quantityUpper, PositionDirection.SHORT)
    
    new OptionPosition(List(lowerLeg, upperLeg))
  }
  
  def longPutRatioBackspread(symbol: String, strikePriceLower: Double, strikePriceUpper: Double, expirationDate: Instant, entryPriceLower: Double, entryPriceUpper: Double, quantityLower: Int, quantityUpper: Int): OptionPosition = {
    val lower = new Option(symbol, OptionType.PUT, strikePriceLower, expirationDate)
    val lowerLeg = new OptionLeg(lower, entryPriceLower, quantityLower, PositionDirection.LONG)
    
    val upper = new Option(symbol, OptionType.PUT, strikePriceUpper, expirationDate)
    val upperLeg = new OptionLeg(upper, entryPriceUpper, quantityUpper, PositionDirection.SHORT)
    
    new OptionPosition(List(lowerLeg, upperLeg))
  }
  
  def shortPutRatioBackspread(symbol: String, strikePriceLower: Double, strikePriceUpper: Double, expirationDate: Instant, entryPriceLower: Double, entryPriceUpper: Double, quantityLower: Int, quantityUpper: Int): OptionPosition = {
    val lower = new Option(symbol, OptionType.PUT, strikePriceLower, expirationDate)
    val lowerLeg = new OptionLeg(lower, entryPriceLower, quantityLower, PositionDirection.SHORT)
    
    val upper = new Option(symbol, OptionType.PUT, strikePriceUpper, expirationDate)
    val upperLeg = new OptionLeg(upper, entryPriceUpper, quantityUpper, PositionDirection.LONG)
    
    new OptionPosition(List(lowerLeg, upperLeg))
  }
  
  def coveredCall(symbol: String, strikePrice: Double, expirationDate: Instant, entryPriceCall: Double, entryPriceStock: Double, quantity: Int): OptionPosition = {
    val call = new Option(symbol, OptionType.CALL, strikePrice, expirationDate)
    val callLeg = new OptionLeg(call, entryPriceCall, quantity, PositionDirection.SHORT)
    
    val stockPosition = new StockPosition(symbol, entryPriceStock, quantity*100, PositionDirection.LONG)
    
    new OptionPosition(List(callLeg), stockPosition)
  }
  
  def coveredPut(symbol: String, strikePrice: Double, expirationDate: Instant, entryPricePut: Double, entryPriceStock: Double, quantity: Int): OptionPosition = {
    val put = new Option(symbol, OptionType.PUT, strikePrice, expirationDate)
    val putLeg = new OptionLeg(put, entryPricePut, quantity, PositionDirection.SHORT)
    
    val stockPosition = new StockPosition(symbol, entryPriceStock, quantity*100, PositionDirection.SHORT)
    
    new OptionPosition(List(putLeg), stockPosition)
  }
  
  def longFence(symbol: String, strikePriceCall: Double, strikePricePut: Double, expirationDate: Instant, entryPriceCall: Double, entryPricePut: Double, entryPriceStock: Double, quantity: Int): OptionPosition = {
    val call = new Option(symbol, OptionType.CALL, strikePriceCall, expirationDate)
    val callLeg = new OptionLeg(call, entryPriceCall, quantity, PositionDirection.SHORT)
    
    val put = new Option(symbol, OptionType.PUT, strikePricePut, expirationDate)
    val putLeg = new OptionLeg(put, entryPricePut, quantity, PositionDirection.LONG)
    
    val stockPosition = new StockPosition(symbol, entryPriceStock, quantity*100, PositionDirection.LONG)
    
    new OptionPosition(List(callLeg, putLeg), stockPosition)
  }
  
  def shortFence(symbol: String, strikePriceCall: Double, strikePricePut: Double, expirationDate: Instant, entryPriceCall: Double, entryPricePut: Double, entryPriceStock: Double, quantity: Int): OptionPosition = {
    val call = new Option(symbol, OptionType.CALL, strikePriceCall, expirationDate)
    val callLeg = new OptionLeg(call, entryPriceCall, quantity, PositionDirection.LONG)
    
    val put = new Option(symbol, OptionType.PUT, strikePricePut, expirationDate)
    val putLeg = new OptionLeg(put, entryPricePut, quantity, PositionDirection.SHORT)
    
    val stockPosition = new StockPosition(symbol, entryPriceStock, quantity*100, PositionDirection.SHORT)
    
    new OptionPosition(List(callLeg, putLeg), stockPosition)
  }
}