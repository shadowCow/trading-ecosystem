## Plan

### Layered Architecture
Start with a decision maker for a single market in isolation, with a fixed strategy.

Then we can build on top of that, with decision makers across markets, across strategies, etc.

### Single market problem
We have datasets like:
```(features, reward)```
Where features represents one or more columns, and reward is a value we can get by making a decision.
e.g.
```text
f1, 2.5
f2, -1.7
f3, 0.1
f4, 0
f5, -0.65
```

The job of the decision maker for a single market is to pick a decision from {-1, 0, 1} for each row in the dataset.

The goal is some variation of maximizing the cumulative reward, i.e. sum(reward(i) * decision(i)) over all i.
 
This is just a classification problem with a custom loss function?  
Or a reinforcement learning problem with a static environment and single state?
Or a clustering problem where we evaluate a metric on the resulting clusters?

#### Goal for learning...
We want the decision maker to learn a decision rule that is 'good' (not necessarily optimal).

Performance is less important than robustness.  Given some decision model, (in)sensitivity of the model parameters is more important than total performance.

Are we just separating the input space into 3 regions? (the -1, 0, and 1 regions).
In which case, maybe the goal is to find the separation that is some combination of...
- maximum reward
- minimum number separating planes (or curves) - less overfitting
- maximum distance between points in regions and their boundaries - more robust



### (Hyper) Parameters
The inputs to the decision maker are the raw data, the engineered features, and the reward.

The reward is directly a function of the trading strategy.  So trading strategy is a fixed input to the decision maker.

To start, we can 'loop through' a lot of engineered features and a lot of trading strategies, and their combinations.

Later, we could incorporate that selection into the decision maker.


### Stages
At the moment, there seem to be these stages...
1. Add features and strategies to the raw data.
2. Train the decision model.
3. Cross validate the decision model.
4. Store the decision model somewhere.

Stage 1 seems like a Spark thing at the moment.
Stage 2 seems like a TensorFlow or DeepLearning4J thing.
Stage 3 seems like either part of Stage 2 framework or maybe back to Spark.
Stage 4 seems like a MongoDB thing.

### Next
Well, maybe we forget optimality and fancy techniques and make this simple...

We need...
- A way to specify the structure of the decision model such that there are adjustable parameters.
- A way to evaluate the model for a given parameter set.
- A way to search the parameter space for 'good' models.

Then we just save the good models.

That doesn't need to be complicated.

What we need is a flexible enough way of defining the decision model such that we can automate a broad search for something that works.

And then we need a GOOD evaluation procedure.