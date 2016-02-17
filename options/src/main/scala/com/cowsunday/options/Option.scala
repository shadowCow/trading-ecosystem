package com.cowsunday.options

import java.time._

/**
 * Represents a single option.
 */
class Option(val underlyingSymbol: String, val optionType: OptionType.Value, val strikePrice: Double, val expirationDate: Instant) {

}