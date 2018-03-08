# Problem Definition
Find a trading strategy and the associated model that maximizes risk adjusted returns.

Generally speaking, we have two main components to deal with:
- Trading strategy <=> Loss function to optimize
- Trading decision <=> Prediction classes

That is - a given trading strategy will have a specific loss function.  Each trading strategy will likely have a slightly different loss function depending on its specifics.

Then, given a trading strategy, we have a decision as to what action to take (Buy, Sell, Neutral, etc).

The trading decision is linked to the trading strategy.  For example if we are doing a classification algorithm, we need to label our input data with classes based on the trading strategy.
 
## Approaches
#### Take Trading Strategy as a given
We can list out a number of practical trading strategies.

For each one, we specify the loss function.

Then, the problem becomes a search for good prediction model for that trading strategy.

We need to be able to vary the input data, features, and model used.

Assuming we have a list of strategies, presumably we could vary that as well to do some kind of automated selection process.

##### Classification
To build classification models, we will need to label the training data.

For some strategies, maybe this won't make sense.  E.g. if I want to be in the market for 'days with positive expected value' - it isn't possible to label such days.  For a strategy like that - we still have a loss function, but we don't have labeled data, so it won't be a classification problem...

##### Unsupervised Learning
For a strategy such as 'be in market for days with positive expected value' - there are no labels.

For such a strategy, we will probably need to use unsupervised learning or reinforcement learning.

Perhaps clustering, expectation maximization, or neural networks?

#### Moving Forward
Classification will likely be easy to set up and interpret.  That seems like a good place to start.  We can see how far that can get us.  Once we establish the limitations of that, we can consider some of the unsupervised learning techniques.

## Collecting Results
Since we will be doing a large search over these possible inputs and strategies, we need to collect this info to find the 'best' candidates.

We will need a good format for publishing to a database, as well as a database infrastructure to publish to.

## Extensibility
We need to define a flexible and extensible pipeline.

We definitely won't have all the features and strategies figured out up front.

So the main extensibility questions are:
- When we get new input data (new markets, more recent data for existing markets), where does that plugin and how does that affect existing results?
- When we come up with new features, where does that plugin and how does that affect existing results?
- When we come up with new strategies, where does that plugin and how does that affect existing results?

---------------
#Problem Definition part 2
There are two layers to this...
1. Are there are natural properties to the data that allow an edge?  i.e. Can we put on a given strategy every single day with no predictive attempts at all and expect to make money?
2. Can we predict which days are 'better' by some criterion?

If we can find anything for (1) then we likely have something very durable.  If we can layer (2) on top of (1) - that could work very well, as we're working in line with the market's natural tendencies.

(2) could be cast as a classification problem in certain situations.  In general, it is not though.
Its more of a process of applying 'filters' to do the data based on features to see if we improve some metric.

You could say that, given a feature, is there a value we can split on to get one group with 'good' results and one group with 'bad' results?  We don't define the boundary of good vs bad up front (like in classification).  Instead the learning procedure searches for a boundary that maximized the distance of 'good' to 'bad'.

I think this makes more sense than classification.

___________
# Techniques
#### Classification
If we define a highly structured strategy, we can label our bars as 'winners' and 'losers', and then train a classifier to get high precision.

#### Regression Tree
We can let the tree define splitting rules on various features to try to end up with rules that lead to 'high expectation' groups.


## Commonalities
In any case, the common element here is to generate some useful features.