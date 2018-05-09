import unittest
from ..src.pipeline import run_model_pipeline

class TestPipeline(unittest.TestCase):

    def test_pipeline(self):
        # not sure what these config objects look like yet.
        dataset_config = {stuff:'hi'}
        model_config = {thing:'yo'}

        outcome = run_model_pipeline(dataset_config, model_config)

        self.assertEqual(outcome, {is_success:True})


if __name__ == '__main__':
    unittest.main()
