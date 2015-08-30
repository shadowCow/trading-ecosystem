__author__ = 'dwadeson'

from options import option_position_metrics
from options import option_value_interval
from options import distribution

import unittest
import math
from scipy import integrate


class TestOptionPositionMetrics(unittest.TestCase):
    def test_get_option_value_intervals(self):
        leg1 = {
            'option': {
                'type': 'Call',
                'strike': 100,
                'price': 5
            },
            'direction': 'Long'
        }
        option_position = [
            leg1
        ]
        my_distribution = distribution.Distribution(95, 115, lambda x: x, lambda x: x)
        actual_result = option_position_metrics.get_option_value_intervals(option_position, my_distribution)
        expected_interval_1 = option_value_interval.OptionValueInterval(95, 100, -5, -5)
        expected_interval_2 = option_value_interval.OptionValueInterval(100, 115, -5, 10)
        self.assertEqual(2, len(actual_result))
        self.assertAlmostEqual(expected_interval_1.start, actual_result[0].start, 4)
        self.assertAlmostEqual(expected_interval_1.end, actual_result[0].end, 4)
        self.assertAlmostEqual(expected_interval_1.value_at_start, actual_result[0].value_at_start, 4)
        self.assertAlmostEqual(expected_interval_1.value_at_end, actual_result[0].value_at_end, 4)
        self.assertAlmostEqual(expected_interval_1.slope, actual_result[0].slope, 4)
        self.assertAlmostEqual(expected_interval_1.zero_point, actual_result[0].zero_point, 4)
        self.assertAlmostEqual(expected_interval_2.start, actual_result[1].start, 4)
        self.assertAlmostEqual(expected_interval_2.end, actual_result[1].end, 4)
        self.assertAlmostEqual(expected_interval_2.value_at_start, actual_result[1].value_at_start, 4)
        self.assertAlmostEqual(expected_interval_2.value_at_end, actual_result[1].value_at_end, 4)
        self.assertAlmostEqual(expected_interval_2.slope, actual_result[1].slope, 4)
        self.assertAlmostEqual(expected_interval_2.zero_point, actual_result[1].zero_point, 4)

        leg2 = {
            'option': {
                'type': 'Call',
                'strike': 90,
                'price': 15
            },
            'direction': 'Long'
        }
        option_position2 = [leg2]
        my_distribution = distribution.Distribution(70, 130, lambda x: (x - 70) / (130 - 70), lambda x: 1 / (130 - 70))
        actual_result = option_position_metrics.get_option_value_intervals(option_position2, my_distribution)
        expected_interval_1 = option_value_interval.OptionValueInterval(70, 90, -15, -15)
        expected_interval_2 = option_value_interval.OptionValueInterval(90, 130, -15, 25)
        self.assertEqual(2, len(actual_result))
        self.assertAlmostEqual(expected_interval_1.start, actual_result[0].start, 4)
        self.assertAlmostEqual(expected_interval_1.end, actual_result[0].end, 4)
        self.assertAlmostEqual(expected_interval_1.value_at_start, actual_result[0].value_at_start, 4)
        self.assertAlmostEqual(expected_interval_1.value_at_end, actual_result[0].value_at_end, 4)
        self.assertAlmostEqual(expected_interval_1.slope, actual_result[0].slope, 4)
        self.assertAlmostEqual(expected_interval_1.zero_point, actual_result[0].zero_point, 4)
        self.assertAlmostEqual(expected_interval_2.start, actual_result[1].start, 4)
        self.assertAlmostEqual(expected_interval_2.end, actual_result[1].end, 4)
        self.assertAlmostEqual(expected_interval_2.value_at_start, actual_result[1].value_at_start, 4)
        self.assertAlmostEqual(expected_interval_2.value_at_end, actual_result[1].value_at_end, 4)
        self.assertAlmostEqual(expected_interval_2.slope, actual_result[1].slope, 4)
        self.assertAlmostEqual(expected_interval_2.zero_point, actual_result[1].zero_point, 4)

        # short call
        leg2 = {
            'option': {
                'type': 'Call',
                'strike': 85,
                'price': 20
            },
            'direction': 'Short'
        }
        option_position2 = [leg2]
        my_distribution = distribution.Distribution(70, 130, lambda x: (x - 70) / (130 - 70), lambda x: 1 / (130 - 70))
        actual_result = option_position_metrics.get_option_value_intervals(option_position2, my_distribution)
        expected_interval_1 = option_value_interval.OptionValueInterval(70, 85, 20, 20)
        expected_interval_2 = option_value_interval.OptionValueInterval(85, 130, 20, -25)
        self.assertEqual(2, len(actual_result))
        self.assertAlmostEqual(expected_interval_1.start, actual_result[0].start, 4)
        self.assertAlmostEqual(expected_interval_1.end, actual_result[0].end, 4)
        self.assertAlmostEqual(expected_interval_1.value_at_start, actual_result[0].value_at_start, 4)
        self.assertAlmostEqual(expected_interval_1.value_at_end, actual_result[0].value_at_end, 4)
        self.assertAlmostEqual(expected_interval_1.slope, actual_result[0].slope, 4)
        self.assertAlmostEqual(expected_interval_1.zero_point, actual_result[0].zero_point, 4)
        self.assertAlmostEqual(expected_interval_2.start, actual_result[1].start, 4)
        self.assertAlmostEqual(expected_interval_2.end, actual_result[1].end, 4)
        self.assertAlmostEqual(expected_interval_2.value_at_start, actual_result[1].value_at_start, 4)
        self.assertAlmostEqual(expected_interval_2.value_at_end, actual_result[1].value_at_end, 4)
        self.assertAlmostEqual(expected_interval_2.slope, actual_result[1].slope, 4)
        self.assertAlmostEqual(expected_interval_2.zero_point, actual_result[1].zero_point, 4)

    def test_compute_expected_value(self):
        reward_risk_ratio = 2
        win_pct = 0.5
        expected_result = 0.5

        actual_result = option_position_metrics.compute_expected_value(reward_risk_ratio, win_pct)

        self.assertAlmostEqual(expected_result, actual_result, 4)

    def test_compute_reward_risk_ratio(self):
        expected_gain = -1
        expected_loss = 1
        try:
            option_position_metrics.compute_reward_risk_ratio(expected_gain, expected_loss)
        except ValueError:
            """
            ok, we want an error thrown with that input
            """
        else:
            self.fail('negative expected_gain should have caused a ValueError')

        expected_gain = 1
        expected_loss = -1
        try:
            option_position_metrics.compute_reward_risk_ratio(expected_gain, expected_loss)
        except ValueError:
            """
            ok, we want that error thrown here
            """
        else:
            self.fail('negative expected_loss should have caused a ValueError')

        # if both are 0.0 result should be nan
        expected_gain = 0.0
        expected_loss = 0.0
        actual_result = option_position_metrics.compute_reward_risk_ratio(expected_gain, expected_loss)
        self.assertTrue(math.isnan(actual_result))

        # if just loss is 0.0 result should be inf
        expected_gain = 1.0
        expected_loss = 0.0
        actual_result = option_position_metrics.compute_reward_risk_ratio(expected_gain, expected_loss)
        self.assertTrue(math.isinf(actual_result))

        # if just gain is 0.0 result should be 0.0
        expected_gain = 0.0
        expected_loss = 1.0
        expected_result = 0.0
        actual_result = option_position_metrics.compute_reward_risk_ratio(expected_gain, expected_loss)
        self.assertAlmostEqual(expected_result, actual_result, 4)

        # if both > 0, should be a sensible number
        expected_gain = 2.0
        expected_loss = 0.5
        expected_result = 4.0
        actual_result = option_position_metrics.compute_reward_risk_ratio(expected_gain, expected_loss)
        self.assertAlmostEqual(expected_result, actual_result, 4)

    def test_cumulative_expectation(self):
        leg1 = {}
        leg1['option'] = {
            'type': 'Call',
            'strike': 100,
            'price': 5
        }
        leg1['direction'] = 'Long'
        option_position = [
            leg1
        ]
        pdf = lambda x: 1.0 / 15.0
        # its one continuous interval, but the code we are calling doesn't know that
        win_intervals = [[105, 110], [110, 115]]
        actual_result = option_position_metrics.cumulative_expectation(win_intervals, option_position, pdf)
        expected_result = 3.3333
        self.assertAlmostEqual(expected_result, actual_result, 4)

        leg2 = {
            'option': {
                'type': 'Call',
                'strike': 90,
                'price': 15
            },
            'direction': 'Long'
        }
        option_position2 = [leg2]
        pdf = lambda x: 1.0 / (130 - 70)
        win_intervals = [[105, 130]]
        actual_result = option_position_metrics.cumulative_expectation(win_intervals, option_position2, pdf)
        expected_result = 5.2083
        self.assertAlmostEqual(expected_result, actual_result, 4)

        loss_intervals = [[70, 105]]
        actual_result = option_position_metrics.cumulative_expectation(loss_intervals, option_position2, pdf)
        expected_result = -6.875
        self.assertAlmostEqual(expected_result, actual_result, 4)

    def test_compute_max_loss(self):
        leg1 = {
            'option': {
                'type': 'Call',
                'strike': 100,
                'price': 5
            },
            'direction': 'Long'
        }
        option_position = [
            leg1
        ]
        interval1 = option_value_interval.OptionValueInterval(95, 100, -5, -5)
        interval2 = option_value_interval.OptionValueInterval(100, 115, -5, 10)
        actual_result = option_position_metrics.compute_max_loss([interval1, interval2])
        expected_result = -5
        self.assertAlmostEqual(expected_result, actual_result, 4)

        # check when there is no loss at all, but min value is 0
        interval = option_value_interval.OptionValueInterval(105, 115, 0, 10)
        actual_result = option_position_metrics.compute_max_loss([interval])
        expected_result = 0
        self.assertAlmostEqual(expected_result, actual_result, 4)

        # check when there is no loss, min value > 0
        interval = option_value_interval.OptionValueInterval(110, 115, 5, 10)
        actual_result = option_position_metrics.compute_max_loss([interval])
        expected_result = 5
        self.assertAlmostEqual(expected_result, actual_result, 4)

    def test_compute_max_gain(self):
        leg1 = {
            'option': {
                'type': 'Call',
                'strike': 100,
                'price': 5
            },
            'direction': 'Long'
        }
        option_position = [
            leg1
        ]
        interval1 = option_value_interval.OptionValueInterval(95, 100, -5, -5)
        interval2 = option_value_interval.OptionValueInterval(100, 115, -5, 10)
        actual_result = option_position_metrics.compute_max_gain([interval1, interval2])
        expected_result = 10
        self.assertAlmostEqual(expected_result, actual_result, 4)

        # check when there is no gain at all, but max value is 0
        interval = option_value_interval.OptionValueInterval(100, 105, -5, 0)
        actual_result = option_position_metrics.compute_max_gain([interval])
        expected_result = 0
        self.assertAlmostEqual(expected_result, actual_result, 4)

        # check when there is no gain, max value < 0
        interval = option_value_interval.OptionValueInterval(95, 100, -5, -5)
        actual_result = option_position_metrics.compute_max_loss([interval])
        expected_result = -5
        self.assertAlmostEqual(expected_result, actual_result, 4)

    def test_compute_win_pct(self):
        leg1 = {
            'option': {
                'type': 'Call',
                'strike': 100,
                'price': 5
            },
            'direction': 'Long'
        }
        option_position = [
            leg1
        ]
        cdf = lambda x: (x - 95) / (115 - 95)
        interval1 = option_value_interval.OptionValueInterval(95, 100, -5, -5)
        interval2 = option_value_interval.OptionValueInterval(100, 115, -5, 10)
        actual_result = option_position_metrics.compute_win_pct([interval1, interval2], cdf)
        expected_result = 0.5
        self.assertAlmostEqual(expected_result, actual_result, 4)

        # short call
        leg2 = {
            'option': {
                'type': 'Call',
                'strike': 85,
                'price': 20
            },
            'direction': 'Short'
        }
        option_position2 = [leg2]
        my_distribution = distribution.Distribution(70, 130, lambda x: (x - 70) / (130 - 70), lambda x: 1 / (130 - 70))
        interval1 = option_value_interval.OptionValueInterval(70, 85, 20, 20)
        interval2 = option_value_interval.OptionValueInterval(85, 130, 20, -25)
        actual_result = option_position_metrics.compute_win_pct([interval1, interval2], my_distribution.cdf)
        expected_result = 7/12
        self.assertAlmostEqual(expected_result, actual_result, 4)

    def test_compute_loss_intervals(self):
        leg1 = {
            'option': {
                'type': 'Call',
                'strike': 100,
                'price': 5
            },
            'direction': 'Long'
        }
        option_position = [
            leg1
        ]
        interval1 = option_value_interval.OptionValueInterval(95, 100, -5, -5)
        interval2 = option_value_interval.OptionValueInterval(100, 115, -5, 10)
        actual_result = option_position_metrics.compute_loss_intervals([interval1, interval2])
        expected_result = [[95, 100], [100, 105]]
        self.assertListEqual(expected_result, actual_result)

    def test_compute_win_intervals(self):
        leg1 = {
            'option': {
                'type': 'Call',
                'strike': 100,
                'price': 5
            },
            'direction': 'Long'
        }
        option_position = [
            leg1
        ]
        interval1 = option_value_interval.OptionValueInterval(95, 100, -5, -5)
        interval2 = option_value_interval.OptionValueInterval(100, 115, -5, 10)
        actual_result = option_position_metrics.compute_win_intervals([interval1, interval2])
        expected_result = [[105, 115]]
        self.assertListEqual(expected_result, actual_result)

    def test_compute_position_metrics(self):
        leg1 = {
            'option': {
                'type': 'Call',
                'strike': 100,
                'price': 5
            },
            'direction': 'Long'
        }
        option_position = [
            leg1
        ]
        cdf = lambda x: (x - 95) / (115 - 95)
        pdf = lambda x: 1 / (115 - 95)
        my_distribution = distribution.Distribution(95, 115, cdf, pdf)
        actual_result = option_position_metrics.compute_position_metrics(option_position, my_distribution)
        expected_win_pct = 0.5
        expected_rr = 2.5 / 1.875
        expected_max_loss = -5.0
        expected_ev = expected_win_pct * expected_rr - (1 - expected_win_pct)
        self.assertAlmostEqual(expected_win_pct, actual_result['win_pct'], 4)
        self.assertAlmostEqual(expected_max_loss, actual_result['max_loss'], 4)
        self.assertAlmostEqual(expected_rr, actual_result['rr'], 4)
        self.assertAlmostEqual(expected_ev, actual_result['ev'], 4)

    def test_scipy(self):
        result = integrate.quad(lambda x: x, 0, 2)
        self.assertAlmostEqual(2.0, result[0], 4)

    def test_integrate_expected_vaue_function(self):
        leg1 = {}
        leg1['option'] = {
            'type': 'Call',
            'strike': 100,
            'price': 5
        }
        leg1['direction'] = 'Long'
        option_position = [
            leg1
        ]
        pdf = lambda x: 1.0 / 15.0  # uniform
        result = integrate.quad(
            lambda x: option_position_metrics.expected_value_function(
                x, option_position, pdf),
            105,
            115)
        self.assertAlmostEqual(3.3333, result[0], 4)

        leg1['direction'] = 'Short'
        expected_result = -3.3333
        actual_result = integrate.quad(
            lambda x: option_position_metrics.expected_value_function(
                x, option_position, pdf),
            105,
            115)
        self.assertAlmostEqual(expected_result, actual_result[0], 4)

    # finish this for completeness
    def test_expected_value_function(self):
        pdf = lambda x: 1.0 / 15.0  # uniform
        leg1 = {}
        leg1['option'] = {
            'type': 'Call',
            'strike': 100,
            'price': 5
        }
        leg1['direction'] = 'Long'
        option_position = [
            leg1
        ]
        asset_price = 115
        expected_result = 2.0 / 3.0
        actual_result = option_position_metrics.expected_value_function(asset_price, option_position, pdf)
        self.assertAlmostEqual(expected_result, actual_result, 4)

        leg1['direction'] = 'Short'
        expected_result = -2.0 / 3.0
        actual_result = option_position_metrics.expected_value_function(asset_price, option_position, pdf)
        self.assertAlmostEqual(expected_result, actual_result, 4)

    def test_options_value_function(self):
        # long call in the money
        leg1 = {}
        leg1['option'] = {
            'type': 'Call',
            'strike': 100,
            'price': 5
        }
        leg1['direction'] = 'Long'
        option_position = [
            leg1
        ]
        asset_price = 115
        actual_result = option_position_metrics.position_value_function(asset_price, option_position)
        expected_result = 10
        self.assertAlmostEqual(expected_result, actual_result, 4)

        # long call at break even price
        asset_price = 105
        actual_result = option_position_metrics.position_value_function(asset_price, option_position)
        expected_result = 0
        self.assertAlmostEqual(expected_result, actual_result, 4)

        # long call at the money
        asset_price = 100
        actual_result = option_position_metrics.position_value_function(asset_price, option_position)
        expected_result = -5
        self.assertAlmostEqual(expected_result, actual_result, 4)

        # long call out of the money
        asset_price = 90
        actual_result = option_position_metrics.position_value_function(asset_price, option_position)
        expected_result = -5
        self.assertAlmostEqual(expected_result, actual_result, 4)

        # short call
        leg1['direction'] = 'Short'

        # in the money
        asset_price = 115
        actual_result = option_position_metrics.position_value_function(asset_price, option_position)
        expected_result = -10
        self.assertAlmostEqual(expected_result, actual_result, 4)

        # at break even price
        asset_price = 105
        actual_result = option_position_metrics.position_value_function(asset_price, option_position)
        expected_result = 0
        self.assertAlmostEqual(expected_result, actual_result, 4)

        # at the money
        asset_price = 100
        actual_result = option_position_metrics.position_value_function(asset_price, option_position)
        expected_result = 5
        self.assertAlmostEqual(expected_result, actual_result, 4)

        # out of the money
        asset_price = 90
        actual_result = option_position_metrics.position_value_function(asset_price, option_position)
        expected_result = 5
        self.assertAlmostEqual(expected_result, actual_result, 4)

        # long put in the money
        leg1['option'] = {
            'type': 'Put',
            'strike': 100,
            'price': 5
        }
        leg1['direction'] = 'Long'
        option_position = [
            leg1
        ]
        asset_price = 85
        actual_result = option_position_metrics.position_value_function(asset_price, option_position)
        expected_result = 10
        self.assertAlmostEqual(expected_result, actual_result, 4)

        # long put at break even price
        asset_price = 95
        actual_result = option_position_metrics.position_value_function(asset_price, option_position)
        expected_result = 0
        self.assertAlmostEqual(expected_result, actual_result, 4)

        # long put at the money
        asset_price = 100
        actual_result = option_position_metrics.position_value_function(asset_price, option_position)
        expected_result = -5
        self.assertAlmostEqual(expected_result, actual_result, 4)

        # long put out of the money
        asset_price = 110
        actual_result = option_position_metrics.position_value_function(asset_price, option_position)
        expected_result = -5
        self.assertAlmostEqual(expected_result, actual_result, 4)

        # short put in the money
        leg1['direction'] = 'Short'
        asset_price = 85
        actual_result = option_position_metrics.position_value_function(asset_price, option_position)
        expected_result = -10
        self.assertAlmostEqual(expected_result, actual_result, 4)

        # short put at break even price
        asset_price = 95
        actual_result = option_position_metrics.position_value_function(asset_price, option_position)
        expected_result = 0
        self.assertAlmostEqual(expected_result, actual_result, 4)

        # short put at the money
        asset_price = 100
        actual_result = option_position_metrics.position_value_function(asset_price, option_position)
        expected_result = 5
        self.assertAlmostEqual(expected_result, actual_result, 4)

        # short put out of the money
        asset_price = 110
        actual_result = option_position_metrics.position_value_function(asset_price, option_position)
        expected_result = 5
        self.assertAlmostEqual(expected_result, actual_result, 4)

        # add some more functions to test the computation
        # for example, add long/short straddles and strangles, long/short condors and butterflies, etc.
        # to make sure that multi-leg positions compute correctly


if __name__ == '__main__':
    unittest.main()
