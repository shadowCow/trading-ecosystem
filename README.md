# trading-ecosystem

modules:

data-model - language independent abstract data model.  language specific data models are built from this.  this defines all the objects shared by the different modules.

price-data-store - defines interfaces to access price data for various modules.

backtester - module for performing strategy backtests.  runs as a restful api server.

option-optimizer - module for finding optimal option positions.  runs as a restful api server.

pattern-searcher - module for retrieving segments of price data that match specified patterns.  runs as a restful api server.

visualization - module for displaying data from the various backend servers.

This project is built with gradle.
