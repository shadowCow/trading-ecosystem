# ML Steps
## Stage 1
### Prepare data
- Load raw data from file.
- If needed, process data to create features and labels.

## Stage 2
Stage 2 may be repeated k times for Cross Validation, or we may be doing 'Split data' once, and then all the remaining steps k times.
### Split data for training and testing
- Random splits for k = 3 (or whatever is appropriate) so that we can Cross Validate

### Create an Estimator (e.g. LogisticRegression)
- Configure Estimator parameters

### Fit the model

### Test the model on holdout data

### Evaluate the model
- score it

# Reworking my Pipeline for Dataset/Dataframe Api
Ok, the old stuff was based around RDDs.

But now spark sql is the thing.  So I'm gonna rework everything to use that paradigm.

Lets toss out the old stuff, and just have a `transform` package.

Within that, we can have files/classes for the different types of `Transformer`s.

Based on previous work, loosely speaking, we have `Bar`,`Window`,`Rank` and maybe some others.

I think the way we should proceed here is... rather than just copying the old stuff... we should look at the features we want to generate and code from there.

