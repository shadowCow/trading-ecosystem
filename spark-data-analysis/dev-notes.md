- need to rewrite all the transformations to use DataFrames instead of RDD.  Keep PriceBar though, as that is the 'type' for each row.
- lets make a full end to end thing of a feature selection, model selection, model run and validation and results store.  
once we get a full setup, we can look to extend and refine the various pieces.
- for binary classification, i guess we need 2 stages... 
  first we need to process the data into the format we need
  then we run the binary classification on it.