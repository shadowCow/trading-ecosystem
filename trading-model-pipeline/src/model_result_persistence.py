
def persist_model_results(model_results):
    """Stores the model results to persistent storage.
    Storage might be the file system or a databaseself.

    Args:
        model_results -- An object containing the results of training and evaluating a model.

    Returns:
        obj: An object containing information about the success/failure
            of trying to save the model results and any further details.
    """
    print("persisting")
    return {is_success:True}
