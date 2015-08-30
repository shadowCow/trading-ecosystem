__author__ = 'dwadeson'


import unittest
from options import option_position_analysis
from options import option_factory
from options import distribution
from options.option_position_results import OptionPositionResults
from options.position_metrics import PositionMetrics


class TestOptionPositionAnalysis(unittest.TestCase):

    def test_count_long_calls(self):
        leg1 = option_factory.long_call(100, 5)
        leg2 = option_factory.long_call(200, 2)
        leg3 = option_factory.short_call(50, 1)

        expected_result = 2
        actual_result = option_position_analysis.count_long_calls([leg1, leg2, leg3])
        self.assertAlmostEqual(expected_result, actual_result, 1)

    def test_count_short_calls(self):
        leg1 = option_factory.long_call(100, 5)
        leg2 = option_factory.long_call(200, 2)
        leg3 = option_factory.short_call(50, 1)

        expected_result = 1
        actual_result = option_position_analysis.count_short_calls([leg1, leg2, leg3])
        self.assertAlmostEqual(expected_result, actual_result, 1)

    def test_is_position_allowed(self):
        # more long legs than short should always be true
        leg1 = option_factory.long_call(100, 5)
        leg2 = option_factory.long_call(200, 2)
        leg3 = option_factory.short_call(50, 1)

        self.assertTrue(option_position_analysis.is_position_allowed([leg1, leg2, leg3], True))
        self.assertTrue(option_position_analysis.is_position_allowed([leg1, leg2, leg3], False))

        # same number long as short should always be true
        leg4 = option_factory.short_call(75, 1)
        self.assertTrue(option_position_analysis.is_position_allowed([leg1, leg2, leg3, leg4], True))
        self.assertTrue(option_position_analysis.is_position_allowed([leg1, leg2, leg3, leg4], False))

        # extra short legs should be false when allow_uncapped_risk is false
        leg5 = option_factory.short_call(150, 3)
        self.assertTrue(option_position_analysis.is_position_allowed([leg1, leg2, leg3, leg4, leg5], True))
        self.assertFalse(option_position_analysis.is_position_allowed([leg1, leg2, leg3, leg4, leg5], False))

    def test_update_best_positions_list(self):
        opr1 = TestOptionPositionAnalysis.create_dummy_opr(0.5)
        opr2 = TestOptionPositionAnalysis.create_dummy_opr(1)
        opr3 = TestOptionPositionAnalysis.create_dummy_opr(0.75)
        opr4 = TestOptionPositionAnalysis.create_dummy_opr(-5)
        opr5 = TestOptionPositionAnalysis.create_dummy_opr(-2)
        opr6 = TestOptionPositionAnalysis.create_dummy_opr(0)
        opr7 = TestOptionPositionAnalysis.create_dummy_opr(0.8)

        max_list_size = 5
        best_positions = [opr1, opr2]
        option_position_analysis.update_best_positions_list(opr3, best_positions, max_list_size)
        expected_result = [opr2, opr3, opr1]
        self.assertListEqual(expected_result, best_positions)

        option_position_analysis.update_best_positions_list(opr4, best_positions, max_list_size)
        expected_result = [opr2, opr3, opr1, opr4]
        self.assertListEqual(expected_result, best_positions)

        option_position_analysis.update_best_positions_list(opr5, best_positions, max_list_size)
        expected_result = [opr2, opr3, opr1, opr5, opr4]
        self.assertListEqual(expected_result, best_positions)

        option_position_analysis.update_best_positions_list(opr6, best_positions, max_list_size)
        expected_result = [opr2, opr3, opr1, opr6, opr5]
        self.assertListEqual(expected_result, best_positions)

        option_position_analysis.update_best_positions_list(opr7, best_positions, max_list_size)
        expected_result = [opr2, opr7, opr3, opr1, opr6]
        self.assertListEqual(expected_result, best_positions)

    def test_compute_best_one_leg_positions(self):
        # calls
        c1 = option_factory.call(80, 25)
        c2 = option_factory.call(85, 20)
        c3 = option_factory.call(90, 15)
        c4 = option_factory.call(95, 10)
        c5 = option_factory.call(100, 5)
        c6 = option_factory.call(105, 3)
        c7 = option_factory.call(110, 1.5)
        c8 = option_factory.call(115, 1)
        c9 = option_factory.call(120, 0.5)
        # puts
        p1 = option_factory.put(80, 0.5)
        p2 = option_factory.put(85, 1)
        p3 = option_factory.put(90, 1.5)
        p4 = option_factory.put(95, 3)
        p5 = option_factory.put(100, 5)
        p6 = option_factory.put(105, 10)
        p7 = option_factory.put(110, 15)
        p8 = option_factory.put(115, 20)
        p9 = option_factory.put(120, 25)

        options = [c1, c2, c3, c4, c5, c6, c7, c8, c9, p1, p2, p3, p4, p5, p6, p7, p8, p9]

        my_distribution = distribution.Distribution(70, 130, lambda x: (x - 70) / (130 - 70), lambda x: 1 / (130 - 70))
        allow_uncapped_risk = True
        num_to_return = 5
        best_positions = option_position_analysis.compute_best_positions_for_num_legs(1, options, my_distribution, allow_uncapped_risk, num_to_return)
        self.assertEqual(num_to_return, len(best_positions))

        leg1 = best_positions[0].option_position[0]
        self.assertEqual('Short', leg1['direction'])
        self.assertAlmostEqual(25, leg1['option']['price'])
        self.assertAlmostEqual(80, leg1['option']['strike'])
        self.assertEqual('Call', leg1['option']['type'])

        leg2 = best_positions[1].option_position[0]
        self.assertEqual('Short', leg2['direction'])
        self.assertAlmostEqual(25, leg2['option']['price'])
        self.assertAlmostEqual(120, leg2['option']['strike'])
        self.assertEqual('Put', leg2['option']['type'])

        leg3 = best_positions[2].option_position[0]
        self.assertEqual('Short', leg3['direction'])
        self.assertAlmostEqual(20, leg3['option']['price'])
        self.assertAlmostEqual(85, leg3['option']['strike'])
        self.assertEqual('Call', leg3['option']['type'])

        leg4 = best_positions[3].option_position[0]
        self.assertEqual('Short', leg4['direction'])
        self.assertAlmostEqual(20, leg4['option']['price'])
        self.assertAlmostEqual(115, leg4['option']['strike'])
        self.assertEqual('Put', leg4['option']['type'])

        leg5 = best_positions[4].option_position[0]
        self.assertEqual('Short', leg5['direction'])
        self.assertAlmostEqual(15, leg5['option']['price'])
        self.assertAlmostEqual(90, leg5['option']['strike'])
        self.assertEqual('Call', leg5['option']['type'])

        for p in best_positions:
            print(p)
        print('\n\n')

    def test_compute_best_two_leg_positions(self):
        options = TestOptionPositionAnalysis.get_option_chain_1()

        my_distribution = distribution.Distribution(70, 130, lambda x: (x - 70) / (130 - 70), lambda x: 1 / (130 - 70))
        allow_uncapped_risk = True
        num_to_return = 5
        best_positions = option_position_analysis.compute_best_positions_for_num_legs(2, options, my_distribution, allow_uncapped_risk, num_to_return)
        self.assertEqual(num_to_return, len(best_positions))

        op1 = best_positions[0].option_position
        TestOptionPositionAnalysis.validate_leg(self, op1[0], 'Long', 'Call', 100, 5)
        TestOptionPositionAnalysis.validate_leg(self, op1[1], 'Long', 'Put', 100, 5)

        op2 = best_positions[1].option_position
        TestOptionPositionAnalysis.validate_leg(self, op2[0], 'Long', 'Call', 100, 5)
        TestOptionPositionAnalysis.validate_leg(self, op2[1], 'Long', 'Put', 95, 3)

        op3 = best_positions[2].option_position
        TestOptionPositionAnalysis.validate_leg(self, op3[0], 'Long', 'Call', 105, 3)
        TestOptionPositionAnalysis.validate_leg(self, op3[1], 'Long', 'Put', 100, 5)

        op4 = best_positions[3].option_position
        TestOptionPositionAnalysis.validate_leg(self, op4[0], 'Long', 'Call', 105, 3)
        TestOptionPositionAnalysis.validate_leg(self, op4[1], 'Long', 'Put', 95, 3)

        op5 = best_positions[4].option_position
        TestOptionPositionAnalysis.validate_leg(self, op5[0], 'Long', 'Call', 110, 1.5)
        TestOptionPositionAnalysis.validate_leg(self, op5[1], 'Long', 'Put', 90, 1.5)

        for p in best_positions:
            print(p)
        print('\n\n')

    def test_compute_best_three_leg_positions(self):
        options = TestOptionPositionAnalysis.get_option_chain_1()
        my_distribution = distribution.Distribution(70, 130, lambda x: (x - 70) / (130 - 70), lambda x: 1 / (130 - 70))
        allow_uncapped_risk = True
        num_to_return = 5
        best_positions = option_position_analysis.compute_best_positions_for_num_legs(3, options, my_distribution, allow_uncapped_risk, num_to_return)
        self.assertEqual(num_to_return, len(best_positions))

        for p in best_positions:
            print(p)
        print('\n\n')

    def test_compute_best_four_leg_positions(self):
        options = TestOptionPositionAnalysis.get_option_chain_1()
        my_distribution = distribution.Distribution(70, 130, lambda x: (x - 70) / (130 - 70), lambda x: 1 / (130 - 70))
        allow_uncapped_risk = True
        num_to_return = 5
        best_positions = option_position_analysis.compute_best_positions_for_num_legs(4, options, my_distribution, allow_uncapped_risk, num_to_return)
        self.assertEqual(num_to_return, len(best_positions))

        for p in best_positions:
            print(p)
        print('\n\n')

    def test_compute_best_positions(self):
        options = TestOptionPositionAnalysis.get_option_chain_1()

    @staticmethod
    def validate_leg(test_class, leg, expected_direction, expected_type, expected_strike, expected_price):
        test_class.assertEqual(expected_direction, leg['direction'])
        test_class.assertEqual(expected_type, leg['option']['type'])
        test_class.assertAlmostEqual(expected_strike, leg['option']['strike'])
        test_class.assertAlmostEqual(expected_price, leg['option']['price'])

    @staticmethod
    def create_dummy_opr(ev):
        leg1 = option_factory.long_call(100, 5)
        metrics = PositionMetrics(ev, 0, 0, 0, 0)
        return OptionPositionResults([leg1], metrics)

    @staticmethod
    def get_option_chain_1():
        # calls
        c1 = option_factory.call(80, 21)
        c2 = option_factory.call(85, 17)
        c3 = option_factory.call(90, 13)
        c4 = option_factory.call(95, 9)
        c5 = option_factory.call(100, 5)
        c6 = option_factory.call(105, 3)
        c7 = option_factory.call(110, 1.5)
        c8 = option_factory.call(115, 1)
        c9 = option_factory.call(120, 0.5)
        # puts
        p1 = option_factory.put(80, 0.5)
        p2 = option_factory.put(85, 1)
        p3 = option_factory.put(90, 1.5)
        p4 = option_factory.put(95, 3)
        p5 = option_factory.put(100, 5)
        p6 = option_factory.put(105, 9)
        p7 = option_factory.put(110, 13)
        p8 = option_factory.put(115, 17)
        p9 = option_factory.put(120, 21)

        options = [c1, c2, c3, c4, c5, c6, c7, c8, c9, p1, p2, p3, p4, p5, p6, p7, p8, p9]
        return options

if __name__ == '__main__':
    unittest.main()
