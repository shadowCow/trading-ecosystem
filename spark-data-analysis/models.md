# What kinds of Models will we use?

## Decisions to make
The basic trading decisions are...
- Go long
- Go short
- Go neutral

We can view these as just 3 decisions, or we could view them as state dependent...
- From neutral...
  - Go long - incurs transaction cost
  - Stay neutral
  - Go short - incurs transaction cost
- From long...
  - Stay long
  - Go neutral - incurs transaction cost
  - Go short - incurs transaction cost
- From short...
  - Go long - incurs transaction cost
  - Go neutral - incurs transaction cost
  - Stay short

We could also factor in position size, and the decision space becomes extremely large.

In general, our goal is train the model to make good decisions.  The simpler the decisions are, the simpler our task should be.

If the output of the model is a trading decision, then we need a way to score that so we can adequately train the model.

How do we evaluate a trading decision?  
- Was the position profitable?
- How profitable was the position?
- If the position was unprofitable, how unprofitable was it?

Taking a position that loses money shouldn't necessarily be penalized strictly, if the amount of money lost is small.

We would want to penalize large losses heavily.

## Simple example
Say we have model that outputs long, short, neutral for every bar.  We then score the model based on the outcomes given those predictions.  
So if the model scores 'poorly', we adjust the model parameters to get better predictions.

## Evaluation
Scoring is based on trading performance. We can consider trading performance in the aggregate, i.e. just sum up the gains and losses.
Or - we can consider trading performance on a more trade by trade basis, e.g. a big loss when a gain was predicted would be penalized more heavily than a small loss when a gain was predicted.

In any case, we can just define a custom loss function, and use that.


### Binary classification
We can use isolated models.  E.g. a buying model and a shorting model, a 'big up day model' and a 'high probability up day model', etc.

In that case, we are just trying to predict if a bar fits the criteria or not (e.g. is up bar, is >=5x r/r up bar, etc.).
 
Scoring these models is just a matter of checking the accuracy.  i.e. model score is just a 'percentage correct' value.

In order to use these models, we need to format our data as an RDD of LabeledPoints.


### Multiclass classification
We can classify each bar as go long, go short, or go neutral.  Like -1, 0, +1.  Or classify by bins, -2x, -1x, 0, +1x, +2x, etc.  Then we score based on whether things were classified correctly.


