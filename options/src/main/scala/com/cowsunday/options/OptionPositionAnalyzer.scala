package com.cowsunday.options

object OptionPositionAnalyzer {

  def computeBestPositions(optionChain: List[ValuedOption], distribution: DiscreteDistribution, allowUncappedRisk: Boolean) {
    // separate the options into the 4 basic positions.  sort them by expected value - highest first.
    val longCallAnalyses = getAnalyses(optionChain, distribution, OptionType.CALL, PositionDirection.LONG)
    val shortCallAnalyses = getAnalyses(optionChain, distribution, OptionType.CALL, PositionDirection.SHORT)
    val longPutAnalyses = getAnalyses(optionChain, distribution, OptionType.PUT, PositionDirection.LONG)
    val shortPutAnalyses = getAnalyses(optionChain, distribution, OptionType.PUT, PositionDirection.SHORT)
    
    // try constructing positions by starting with the best options and adding other 'best' options.
    
    
    
  }
  
  private def getLongCallAnalyses(optionChain: List[ValuedOption], distribution: DiscreteDistribution): List[OptionPositionAnalysis] = {
    getAnalyses(optionChain, distribution, OptionType.CALL, PositionDirection.LONG)
  }
  
  private def getAnalyses(optionChain: List[ValuedOption], distribution: DiscreteDistribution, desiredOptionType: OptionType.Value, desiredPositionDirection: PositionDirection.Direction): List[OptionPositionAnalysis] = {
    optionChain.filter { 
      o => o.option.optionType == desiredOptionType
    }.map { 
      o => {
        val position = new OptionPosition(new OptionLeg(o.option, o.price, 1, desiredPositionDirection)) 
        val metrics = OptionPositionMetrics.computeMetrics(position, distribution)
        new OptionPositionAnalysis(position, distribution, metrics)
      } 
    }.sortBy { 
      analysis => analysis.metrics.expectedValue
    }.reverse
  }
  
  private def createImprovedPosition(analyzedPosition: OptionPositionAnalysis, 
                             longCalls: List[OptionPositionAnalysis],
                             shortCalls: List[OptionPositionAnalysis],
                             longPuts: List[OptionPositionAnalysis],
                             shortPuts: List[OptionPositionAnalysis]) {
    
    
    
  }
}