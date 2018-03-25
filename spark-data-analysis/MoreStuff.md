# More stuff

### Constraints
- Trading decisions are made in daily increments.  
i.e. After the close ,the system decides what to do the next day.

### Measuring Performance
There are several ways to measure performance.

- Single day performance - Price change divided by stop size.  
Then we calculate expected value.  
 
- Multi-day performance.  This is tricky.  
E.g. if we have a trailing stop, it may be unlikely to get triggered on any single day - but may take a sequence of days.

- Direct equity measure.  Actual equity growth like a real account.

### System
Ultimately, we need to decide each day...
- Position to have in each market.

If we can do this directly, that would be ideal.
Really we just need to get the features to make decisions with.
Then figure out how to combine those together into decisions.

So we could break development into two separate things maybe.

We'd need
- Feature engineering and evaluation
- Decision making and evaluation

Something like that anyway.  Depending on how we build the system, it may perform feature selection itself.

### Outcome
It sounds best to basically just do 'Simulation' as much as possible.
i.e. build the system as it would actually work in practice and then optimize the decision parameters.
(Neural network or similar thing).

So, for feature engineering, we'd need a way to evaluate features.

Well, i guess it would be best to just include them in all kinds of combinations and see what happens.