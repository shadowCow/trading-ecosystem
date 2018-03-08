# Tradable Situations
In order to build some classification models, we need to define some tradable situations.

Then the classification model is just trying to predict whether the next period is or is not a tradable situation (or which class of tradable situation it belongs to).

### Long/Short Only
The simplest case is a long or short only situation.  

Lets just assume long only, and any discussion pertains analogously to short only.

So we need to specify some trading situations.  Then we can label the data (1.0 if it is that trading situation, 0.0 if not.).  Then we can run our model training against that data.

A trading situation must take into account realistic entry and exit, as well as risk control.

It should be easy to estimate real dollar performance based on just the model metrics.

##### Realistic Entry and Exit
We can't just enter and exit at 'any price'.

The prices we 'know' we can get are Opens and arbitrary stop prices.

So for example...
- We can enter on next open, close on following open
- We can enter on next open, close at stop above open or below open (but not both if same bar...), and/or close on following open
- We can enter on stop price, close at following open
- We can enter on stop price, close at stop price (but not both if same bar...)

We'd like to be able to use theoretical stops/targets, but if they 'both get hit in the same bar', we can't.  One way around this could be to call such bars 'failed'.  i.e. We assume the stop loss is triggered first.

There is also the caveat that single day behavior may not carry over to the subsequent open.  So we may miss out on some important single day opportunities.  It might be worth trying to hack some attempt at a single day outcome, even trying to 'exit on close'.  Hard to say.

It might also make sense to just predict 'losing' situations.  If we have a classifier that helps us avoid the worst situations, we could look at the expectation of the other situations.

### Long only trading situations
- Open to open with stop.  Desire open to open greater than or equal to some multiple of the stop size.

### Real money evaluation
One thing to consider when doing real money evaluation...

The trading results depend on pct correct as well as the expected loss when wrong.  The expected loss when wrong has a big impact on profitability.

If we can bake that into the trading situations we are training on, that would be ideal.

If we do something like (is Nx-multiple of stop), we care about the expectation of when we predicted yes, but were wrong, as well as the the rate at which we predicted yes and were right.

Its going to be tricky to include expectation...

Maybe we can bake that into the model scoring?  i.e. don't just look at the areaUnderROC, look at some proxy for trading performance.
