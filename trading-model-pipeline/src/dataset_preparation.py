
def prepare_dataset_for_model(dataset_config):
    """Locate the dataset and perform any necessary transformations.

    Args:
        dataset_config -- An object containing all details about which data to use
            and what transformations to apply to that data.

    Returns:
        obj: An object that provides information on how to retrieve the dataset.
            Datasets may be used across different processes, so this object
            may be a file uri, or information for a database query, or even
            the raw data itself.
    """
    print("preparing dataset")
    return {file_uri:"/somewhere/on/my/computer"}
