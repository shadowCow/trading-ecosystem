goal: simple A/B testing.  (yes or no response, i.e. 1 or 0 response)
time horizons for prediction: 1,2,3,4,5,10,20 days

tradable prices: opens, midprices


things to predict:
- did the market stay above a price for a certain period of time?
- did the market stay below a price for a certain period of time?
- did the market stay within a range for a certain period of time?
- did the market exceed a range within a certain period of time?
- did the market go up?   (useless?)
- did the market go down? (useless?)
- did the market go up/down by x times as much as a normalizing value?
  
  normalizing values:
  - up -> open-low range
  - down -> open-high range
  - either -> previous bar open/close range
  - either -> previous bar high/low range
  - either -> previous multi-bar range
  - either -> average range over past y bars
- was the market price change in the top x of the past y days?
- was the market price change in the bottom x of the past y days?
- was the market price range in the top x of the past y days?
- was the market price range in the bottom x of the past y days?

^ these are the classifications.  we can either do yes/no for each one, or we can have bins (e.g. 2to1 gain, 3to1 gain, 4to1 gain, etc)

metrics to use for prediction:
- (done) largest price change in x periods? (done)
- (done) rank of price change out of past x periods? (done)
- (done) series of largest price changes (done)
- (done) smallest price change in x periods? (done)
- series of smallest price changes in x periods
- sequence of smallest/largest price changes
- (done) first up day in x periods (done)
- (done) first down day in x periods (done)
- (done) smallest up day in series of up days (done)
- (done) smallest down day in series of down days (done)
- (done) largest up day in series of up days (done)
- (done) largest down day in series of down days (done)
- (done) price change bigger than range of past x periods (done)
- up day bigger than range of past x periods
- down day bigger than range of past x periods
- first up day bigger than any down day in x periods
- first down day bigger than any up day in x periods
- (done) highest price in past x periods (done)
- (done) lowest price in past x periods (done)
- count of new highs made in past x periods
- count of new lows made in past x periods
- (done) ratio of open-close range to high-low range (done)
- ratio of up movement to down movement in past periods (i.e. sum(close - open for up bars) / sum(open - close for down bars))
- ratio of (close - open) to:
  - previous (close - open)
  - previous range (multibar or average)
- ratio of (|open - previous open|) to:
  - previous (close - open)
  - previous range (multibar or average)

we can combine these metrics as well.

-------------- new stuff -------------------

a few types of operators...
- rank          (ordered index - includes things like smallest, largest, etc)
- reduce        (function of several values yielding one value)
- transform     (map set of values to a new set of values)

also, we have single values vs windows...
e.g. biggest single period price change vs. biggest 3 period price change (sliding window)

everything needs to be normalized across markets.  so raw values don't do a lot of good.  
so what translates well across markets....
- rank		   (biggest / smallest value from last x days)
- bins          (top x% of values)
- any normalized value really... (like ratios - day x was 3.2 times the size of day x-1)

## Single Bar Pipeline
A single bar pipeline starts with a series of raw price bars.
We can then perform a series of different transformations, which result in a value for every price bar.
We are simply mapping each price bar to a new value.
Some price bars at the beginning of the series may be truncated since they may not have sufficient prior bars to compute values sliding windows.
E.g. for a sliding window of 5, the first 4 values in the series will be dropped since there will not be 5 bars in a row until the 5th bar.

### Data transformations
#### Single Bar
We can compute values within a single price bar.
These take no parameters.
e.g.
- [close - open]
- [|close - open| / (high - low)]

#### Aggregating a series of bars (Aka sliding window)
We can compute values from a series of bars.     
These take a length parameter.  
  e.g.
- [current open - previous open (2 period window)]
- [high - high 5 periods ago]
- [max high - min low of 4 period window]).

### Ranking
After any transformations to the price bars, we can then convert a set of numerical observations into a set of rankings.
We can do this for a consecutive series of values, e.g. (period 7,8,9,10)
Or for a disjoint series of values e.g. (period 6, 11), (period 3,6,9)
This can be viewed as taking no parameters, or as taking a list of values or a transformation chain on the raw price data as the parameter.
e.g.
- [rank high price ascending]
- [rank 5 period sliding price change descending]

#### Rank Bins
After ranking, we can then bin the values.  
We can do binary operations like max or min or (is it in a specific quartile).
Or we can do classes, like which quartile is it in (1,2,3,4?)
This can be viewed as taking no parameters, or as taking a list of rank values or a transformation chain + ranking.
e.g.
- [is it the biggest value in the set?]
- [is it the smallest value in the set?]
- [is it one of the top x values in the set?]
- [is it one of the bottom x values in the set?]
- [is it one of the middle x values in the set?]
- [which quartile is it in?]

### Value Bins
We can also make bins (or classes) on the raw or transformed numerical values.
This would take the classes as a parameter, and can be viewed having no additional parameters, or having a list of values or a transformation chain as an additional parameter.
e.g.
- [in which element of the set does x belong? {x<0, 0<=x<1, 1<=x<2, 2<=x<3, 3<=x}]

## Multi-period Pipeline
We can also aggregate sets of bars together, and then apply the Single Bar Pipeline on those aggregations.
For example, we could reduce each 'week' of data (M-F, 5 bars) to a single period by applying some function.

### Rehashing on 10-10-16
(Sequence of) PriceBars -> Single Data Points -> Binary or Binning or Ranking
So the data transformation stage itself has stages.

##### Stage 1
The first stage is to transform the PriceBars themselves, with no sliding windows.  Some possible (though not required) transformations:
 - Single data point (just the high, or open, e.g.)
 - Multi data point (the high and the low, e.g.)
 - Single transformed data point (the high-low range, e.g.)
 - Multi transformed data point (the high-low range and open-close range, e.g.)

Note that pipeline stages after this one may not be expecting PriceBar input, so may need to consider that?
Or we may need a generic way to pass either a point, or a tuple of points, or a PriceBar, and have the later pipeline operations understand how to handle that properly?

##### Stage 2 - Sliding Windows
The second stage is to do sliding windows.  We do some transformation to the data sequence.
This can be applying some function to our data point to make another data point (like double -> double), or it could be a ranking or binning operation (double -> int ... or double -> class)
E.g.
 - rolling average
 - rolling highest / lowest
 - rank within the window
 - bin within the window
 - count within the window

##### Stage 3 - Multi-series Comparisons
The third stage is to compare multiple series.  
We have two cases:
 1. We are comparing two series.  The result will be that: series 1 is [ less than, equal to, or greater than ] series 2.  Numerically, the result of the comparison is [ -1, 0, or 1 ].
 2. We are comparison more than two series.  The result will either need to be a ranking or a binning.  In the case of a ranking, the result will be that we map each series to its rank amongst the series at that index, for each index.  In the case of binning, we map each series to one of a set of classes, for each index.

Now, since these various transformations may have different input/output types, we need a way to make sure the pipeline generator knows how to put only compatible types together.  I suppose we could have these various things extend appropriate functional types with regard to their input output, and then we can just register those somewhere in the appropriate list.  Then the pipeline generator would have logic to pull from those lists in a way that is valid.

### Rehashing on 11/1/2016
We're primarily dealing with reductions and comparisons.  Arbitrary transformations seem silly. i.e. mapping one value to another by just passing that single value through some function seems pointless.  

There are some optional processing steps to consider.

#### Pipeline
 1. (Optional) Reduce price bar to a single value
 2. (Optional) Reduce a window of doubles or price bars to a single value.  Some possibilities...
   - average
   - max / min
   - for price bar windows, multi-bar high-low range/open-close range
 3. (Optional) Compare multiple values.  Some possibilities...
   - compare a value for the last bar to a value for the whole window. (the bar may be part of the window or immediately following the window, depending on the use case).
   - compare one market's value to other markets' values.

   And within that, are a few types of comparisons...
   - isMax / isMin / isEqual
   - rank (maybe this includes isMax / isMin and such)
   - bins/classes (includes quartiles and such. also includes pattern classifications?)
   - ratio (this can only be used to compare two values)

Any stage of the pipeline except stage 1 may occur multiple times to generate new features.

## Model Generation
If we define all these operations as functions, then we can automate our model generation.
Various pieces have length parameters, and our functions are parameters.
We can define:
- A list of possible lengths to evaluate.
- The list of transformation functions.
- The list of rank bin functions.
- The list of classes for value bins.  (This may be tricky to define in a static way).

Then we can write a routine to go through all these combinations and select sets of features for our models.
We may also have a list of model types, in which case the routine may also include the model type in the combinations.
For example, we may have a simple linear model, a hierarchical model, and... some other model(s).  We would make different combinations of all the features into these models.

## Model Training And Validation
We can automate training validation as well.  We can split our data into chunks, train the model on certain chunks, and test it on other chunks.
We can pre-generate sets of randomized data using the distribution of the real data, and run the model against those as well.

## Results Storage
Once we've processed a model, we need to store its results.
Items to store:
- Feature meta-data: Sequence of transformation, ordering, and bin functions and any input parameters (length for sliding windows) used to create each feature.   
- Input meta-data: Data set used to train the model (what series (could be several), and what date range)
- Model meta-data:  Which model type was used, what are the model results (ROC, AOC...other ML performance metrics...), and any important info regarding the validation process.

We want this data to be structured, but we need to support adding new functions, data, or models.
A simple solution would be to just have three permanent fields: feature, input, model - each of which is essentially a json object/array or a map/list.
We could then use Mongo or Cassandra to store that data.

## Full Results Evaluation
So we have some process generating models and results for those models, and storing them in our database.
We can then pull results from the database to find which classes of models perform well.
For example, we can look at a specific set of transformations over all possible length inputs, and see how that class of features does as a whole.
Or we an look at a set of features across all models... etc.

## Trading optimization
Depending on the model, we may be able to simulate different risk management policies on randomly generated data.
Or we may need to backtest on real data.
Either way, we can put something like this in at the end to figure out our risk sizing.



### Future
- Irregular time transformations.  E.g. converting data into waves, based on certain aspects of price movement.  Waves may have irregular durations. (Can't do a sliding window)
