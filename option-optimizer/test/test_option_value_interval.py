__author__ = 'dwadeson'

from options import option_value_interval
from options import option_position_metrics
import unittest


class TestOptionValueInterval(unittest.TestCase):

    def test_construction(self):
        leg1 = {}
        leg1['option'] = {
            'type': 'Call',
            'strike': 100,
            'price': 5
        }
        leg1['direction'] = 'Long'
        leg2 = {}
        leg2['option'] = {
            'type':'Put',
            'strike':100,
            'price':5
        }
        leg2['direction'] = 'Long'

        option_position = [
            leg1, leg2
        ]

        # 80 to 100 interval first
        val_func = lambda x: option_position_metrics.position_value_function(x, option_position)
        interval = option_value_interval.OptionValueInterval(80, 100, val_func(80), val_func(100))
        self.assertTrue(interval.is_decreasing_interval())
        self.assertFalse(interval.is_increasing_interval())
        self.assertFalse(interval.is_flat_interval())
        self.assertAlmostEqual(80, interval.start, 4)
        self.assertAlmostEqual(100, interval.end, 4)
        self.assertAlmostEqual(10, interval.value_at_start, 4)
        self.assertAlmostEqual(-10, interval.value_at_end, 4)
        self.assertAlmostEqual(-1, interval.slope, 4)
        self.assertAlmostEqual(90, interval.zero_point, 4)

        positive_sub_interval = interval.get_positive_interval()
        self.assertAlmostEqual(80, positive_sub_interval[0], 4)
        self.assertAlmostEqual(90, positive_sub_interval[1], 4)

        negative_sub_interval = interval.get_negative_interval()
        self.assertAlmostEqual(90, negative_sub_interval[0], 4)
        self.assertAlmostEqual(100, negative_sub_interval[1], 4)

        # 100 to 120 interval second
        interval = option_value_interval.OptionValueInterval(100, 120, val_func(100), val_func(120))
        self.assertFalse(interval.is_decreasing_interval())
        self.assertTrue(interval.is_increasing_interval())
        self.assertFalse(interval.is_flat_interval())
        self.assertAlmostEqual(100, interval.start, 4)
        self.assertAlmostEqual(120, interval.end, 4)
        self.assertAlmostEqual(-10, interval.value_at_start, 4)
        self.assertAlmostEqual(10, interval.value_at_end, 4)
        self.assertAlmostEqual(1, interval.slope, 4)
        self.assertAlmostEqual(110, interval.zero_point, 4)

        positive_sub_interval = interval.get_positive_interval()
        self.assertAlmostEqual(110, positive_sub_interval[0], 4)
        self.assertAlmostEqual(120, positive_sub_interval[1], 4)

        negative_sub_interval = interval.get_negative_interval()
        self.assertAlmostEqual(100, negative_sub_interval[0], 4)
        self.assertAlmostEqual(110, negative_sub_interval[1], 4)

    # test a short option
    def test_short_option(self):
        leg1 = {
            'option': {
                'type': 'Call',
                'strike': 100,
                'price': 5
            },
            'direction': 'Short'
        }
        option_position = [leg1]
        val_func = lambda x: option_position_metrics.position_value_function(x, option_position)

        # 80 to 100 interval
        interval = option_value_interval.OptionValueInterval(80, 100, val_func(80), val_func(100))
        self.assertFalse(interval.is_decreasing_interval())
        self.assertFalse(interval.is_increasing_interval())
        self.assertTrue(interval.is_flat_interval())
        self.assertAlmostEqual(80, interval.start, 4)
        self.assertAlmostEqual(100, interval.end, 4)
        self.assertAlmostEqual(5, interval.value_at_start, 4)
        self.assertAlmostEqual(5, interval.value_at_end, 4)
        self.assertAlmostEqual(0, interval.slope, 4)
        self.assertIsNone(interval.zero_point)

        positive_sub_interval = interval.get_positive_interval()
        self.assertAlmostEqual(80, positive_sub_interval[0], 4)
        self.assertAlmostEqual(100, positive_sub_interval[1], 4)

        negative_sub_interval = interval.get_negative_interval()
        self.assertIsNone(negative_sub_interval)

        # 100 to 120 interval
        interval = option_value_interval.OptionValueInterval(100, 120, val_func(100), val_func(120))
        self.assertTrue(interval.is_decreasing_interval())
        self.assertFalse(interval.is_increasing_interval())
        self.assertFalse(interval.is_flat_interval())
        self.assertAlmostEqual(100, interval.start, 4)
        self.assertAlmostEqual(120, interval.end, 4)
        self.assertAlmostEqual(5, interval.value_at_start, 4)
        self.assertAlmostEqual(-15, interval.value_at_end, 4)
        self.assertAlmostEqual(-1, interval.slope, 4)
        self.assertAlmostEqual(105, interval.zero_point, 4)

        positive_sub_interval = interval.get_positive_interval()
        self.assertAlmostEqual(100, positive_sub_interval[0], 4)
        self.assertAlmostEqual(105, positive_sub_interval[1], 4)

        negative_sub_interval = interval.get_negative_interval()
        self.assertAlmostEqual(105, negative_sub_interval[0], 4)
        self.assertAlmostEqual(120, negative_sub_interval[1], 4)

    # this will test flat slopes off the zero value line
    def test_strangle(self):
        leg1 = {}
        leg1['option'] = {
            'type': 'Call',
            'strike': 105,
            'price': 5
        }
        leg1['direction'] = 'Long'
        leg2 = {}
        leg2['option'] = {
            'type':'Put',
            'strike': 95,
            'price': 5
        }
        leg2['direction'] = 'Long'

        option_position = [
            leg1, leg2
        ]

        # 80 to 95 interval first
        val_func = lambda x: option_position_metrics.position_value_function(x, option_position)
        interval = option_value_interval.OptionValueInterval(80, 95, val_func(80), val_func(95))
        self.assertTrue(interval.is_decreasing_interval())
        self.assertFalse(interval.is_increasing_interval())
        self.assertFalse(interval.is_flat_interval())
        self.assertAlmostEqual(80, interval.start, 4)
        self.assertAlmostEqual(95, interval.end, 4)
        self.assertAlmostEqual(5, interval.value_at_start, 4)
        self.assertAlmostEqual(-10, interval.value_at_end, 4)
        self.assertAlmostEqual(-1, interval.slope, 4)
        self.assertAlmostEqual(85, interval.zero_point, 4)

        positive_sub_interval = interval.get_positive_interval()
        self.assertAlmostEqual(80, positive_sub_interval[0], 4)
        self.assertAlmostEqual(85, positive_sub_interval[1], 4)

        negative_sub_interval = interval.get_negative_interval()
        self.assertAlmostEqual(85, negative_sub_interval[0], 4)
        self.assertAlmostEqual(95, negative_sub_interval[1], 4)

        # 95 to 105 interval second (flat)
        interval = option_value_interval.OptionValueInterval(95, 105, val_func(95), val_func(105))
        self.assertFalse(interval.is_decreasing_interval())
        self.assertFalse(interval.is_increasing_interval())
        self.assertTrue(interval.is_flat_interval())
        self.assertAlmostEqual(95, interval.start, 4)
        self.assertAlmostEqual(105, interval.end, 4)
        self.assertAlmostEqual(-10, interval.value_at_start, 4)
        self.assertAlmostEqual(-10, interval.value_at_end, 4)
        self.assertAlmostEqual(0, interval.slope, 4)
        self.assertAlmostEqual(None, interval.zero_point, 4)

        positive_sub_interval = interval.get_positive_interval()
        self.assertTrue(positive_sub_interval is None)

        negative_sub_interval = interval.get_negative_interval()
        self.assertAlmostEqual(95, negative_sub_interval[0], 4)
        self.assertAlmostEqual(105, negative_sub_interval[1], 4)

        # 105 to 120 interval third
        interval = option_value_interval.OptionValueInterval(105, 120, val_func(105), val_func(120))
        self.assertFalse(interval.is_decreasing_interval())
        self.assertTrue(interval.is_increasing_interval())
        self.assertFalse(interval.is_flat_interval())
        self.assertAlmostEqual(105, interval.start, 4)
        self.assertAlmostEqual(120, interval.end, 4)
        self.assertAlmostEqual(-10, interval.value_at_start, 4)
        self.assertAlmostEqual(5, interval.value_at_end, 4)
        self.assertAlmostEqual(1, interval.slope, 4)
        self.assertAlmostEqual(115, interval.zero_point, 4)

        positive_sub_interval = interval.get_positive_interval()
        self.assertAlmostEqual(115, positive_sub_interval[0], 4)
        self.assertAlmostEqual(120, positive_sub_interval[1], 4)

        negative_sub_interval = interval.get_negative_interval()
        self.assertAlmostEqual(105, negative_sub_interval[0], 4)
        self.assertAlmostEqual(115, negative_sub_interval[1], 4)

    # tests a flat interval which is also a zero interval
    def test_iron_butterfly(self):
        leg1 = {
            'option': {
                'type': 'Call',
                'strike': 100,
                'price': 5
            },
            'direction': 'Short'
        }
        leg2 = {
            'option': {
                'type': 'Put',
                'strike': 100,
                'price': 5
            },
            'direction': 'Short'
        }
        leg3 = {
            'option': {
                'type': 'Call',
                'strike': 105,
                'price': 2.5
            },
            'direction': 'Long'
        }
        leg4 = {
            'option': {
                'type': 'Put',
                'strike': 95,
                'price': 2.5
            },
            'direction': 'Long'
        }

        option_position = [
            leg1, leg2, leg3, leg4
        ]

        # first interval, 80 to 95 (flat)
        val_func = lambda x: option_position_metrics.position_value_function(x, option_position)
        interval = option_value_interval.OptionValueInterval(80, 95, val_func(80), val_func(95))
        self.assertFalse(interval.is_decreasing_interval())
        self.assertFalse(interval.is_increasing_interval())
        self.assertTrue(interval.is_flat_interval())
        self.assertAlmostEqual(80, interval.start, 4)
        self.assertAlmostEqual(95, interval.end, 4)
        self.assertAlmostEqual(0, interval.value_at_start, 4)
        self.assertAlmostEqual(0, interval.value_at_end, 4)
        self.assertAlmostEqual(0, interval.slope, 4)
        self.assertAlmostEqual(option_value_interval.OptionValueInterval.all_zero_point, interval.zero_point, 4)

        positive_sub_interval = interval.get_positive_interval()
        self.assertTrue(positive_sub_interval is None)

        negative_sub_interval = interval.get_negative_interval()
        self.assertTrue(negative_sub_interval is None)

        # second interval, 95 to 100 (increasing)
        interval = option_value_interval.OptionValueInterval(95, 100, val_func(95), val_func(100))
        self.assertFalse(interval.is_decreasing_interval())
        self.assertTrue(interval.is_increasing_interval())
        self.assertFalse(interval.is_flat_interval())
        self.assertAlmostEqual(95, interval.start, 4)
        self.assertAlmostEqual(100, interval.end, 4)
        self.assertAlmostEqual(0, interval.value_at_start, 4)
        self.assertAlmostEqual(5, interval.value_at_end, 4)
        self.assertAlmostEqual(1, interval.slope, 4)
        self.assertAlmostEqual(95, interval.zero_point, 4)

        positive_sub_interval = interval.get_positive_interval()
        self.assertAlmostEqual(95, positive_sub_interval[0], 4)
        self.assertAlmostEqual(100, positive_sub_interval[1], 4)

        negative_sub_interval = interval.get_negative_interval()
        self.assertTrue(negative_sub_interval is None)

        # third interval, 100 to 105 (decreasing)
        interval = option_value_interval.OptionValueInterval(100, 105, val_func(100), val_func(105))
        self.assertTrue(interval.is_decreasing_interval())
        self.assertFalse(interval.is_increasing_interval())
        self.assertFalse(interval.is_flat_interval())
        self.assertAlmostEqual(100, interval.start, 4)
        self.assertAlmostEqual(105, interval.end, 4)
        self.assertAlmostEqual(5, interval.value_at_start, 4)
        self.assertAlmostEqual(0, interval.value_at_end, 4)
        self.assertAlmostEqual(-1, interval.slope, 4)
        self.assertAlmostEqual(105, interval.zero_point, 4)

        positive_sub_interval = interval.get_positive_interval()
        self.assertAlmostEqual(100, positive_sub_interval[0], 4)
        self.assertAlmostEqual(105, positive_sub_interval[1], 4)

        negative_sub_interval = interval.get_negative_interval()
        self.assertTrue(negative_sub_interval is None)

        # fourth interval, 105 to 120 (flat)
        interval = option_value_interval.OptionValueInterval(105, 120, val_func(105), val_func(120))
        self.assertFalse(interval.is_decreasing_interval())
        self.assertFalse(interval.is_increasing_interval())
        self.assertTrue(interval.is_flat_interval())
        self.assertAlmostEqual(105, interval.start, 4)
        self.assertAlmostEqual(120, interval.end, 4)
        self.assertAlmostEqual(0, interval.value_at_start, 4)
        self.assertAlmostEqual(0, interval.value_at_end, 4)
        self.assertAlmostEqual(0, interval.slope, 4)
        self.assertAlmostEqual(option_value_interval.OptionValueInterval.all_zero_point, interval.zero_point, 4)

        positive_sub_interval = interval.get_positive_interval()
        self.assertTrue(positive_sub_interval is None)

        negative_sub_interval = interval.get_negative_interval()
        self.assertTrue(negative_sub_interval is None)