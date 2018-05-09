import prepare_dataset_for_model from dataset_preparation
import train_and_evaluate from model_runner
import persist_model_results from model_result_persistence

def run_model_pipeline(dataset_config, model_config):
    """Prepares a dataset, trains and evaluates a decision model on the dataset,
    and save the results to persistent storage.

    Args:
        dataset_config -- An object containing all details about which data to use
            and what transformations to apply to that data.
        model_config -- An object containing all details about which model to use
            and how to train and evaluate it.

    Returns:
        obj: An object containing information about whether the pipeline run
            was successful and any further details such as errors.
    """
    dataset = prepare_dataset_for_model(dataset_config)
    model_results = train_and_evaluate(model_config, dataset)
    persist_result = persist_model_results(model_results)

    # the outcome will depend on error handling for earlier phases.
    pipeline_outcome = persist_result
    return pipeline_outcome
