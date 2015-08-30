__author__ = 'dwadeson'

from options import option_position_metrics
from options.option_position_results import OptionPositionResults
import itertools


def compute_best_positions(options, distribution, allow_uncapped_risk, max_legs, num_to_return):
    """
    Exhaustive search through all the combinations of options.  Finds the best option positions based on
    the expected value of the position.

    :param options: list of options available to use
    :param distribution: object with properties cdf and pdf (both functions)
    :param allow_uncapped_risk: boolean, if true, allow short calls that are not completely offset.
    :param max_legs: Maximum number of option legs to allow for the position.  Restricted to be 0 < x <= 4
    :param num_to_return: The number of option positions to return.  The top x # of option positions.
    :return: a list of the best option positions.
    """
    max_legs = max(4, max_legs)
    best_positions = []
    for i in range(1, max_legs+1):
        best_positions_for_legs = compute_best_positions_for_num_legs(i, options, distribution, allow_uncapped_risk, num_to_return)
        best_positions.extend(best_positions_for_legs)

    best_positions.sort(key=lambda x: x.metrics.ev, reverse=True)
    while len(best_positions) > num_to_return:
        best_positions.pop()

    return best_positions


def compute_best_positions_for_num_legs(num_legs, options, distribution, allow_uncapped_risk, num_to_return):
    best_positions = []

    option_combos = list(itertools.combinations(options, num_legs))
    direction_combos = list(itertools.product(['Long', 'Short'], repeat=num_legs))

    for option_combo in option_combos:
        for direction_combo in direction_combos:
            option_position = []
            for i in range(0, num_legs):
                leg = {
                    'option': option_combo[i],
                    'direction': direction_combo[i]
                }
                option_position.append(leg)

            if is_position_allowed(option_position, allow_uncapped_risk):
                metrics = option_position_metrics.compute_position_metrics(option_position, distribution)
                option_position_results = OptionPositionResults(option_position, metrics)
                update_best_positions_list(option_position_results, best_positions, num_to_return)

    return best_positions


# broken - does too many permutations
# def compute_improved(max_legs, options, distribution, allow_uncapped_risk, num_to_return):
#     best_positions = []
#     option_legs = []
#     for option in options:
#         option_legs.append({
#             'option': option,
#             'direction': 'Long'
#         })
#         option_legs.append({
#             'option': option,
#             'direction': 'Short'
#         })
#
#     search_tree(best_positions, [], 1, max_legs, option_legs, distribution, allow_uncapped_risk, num_to_return)


# for each partial option position, build the partial option positions.  at each partial, compute stuff, update list
# so we're basically building a tree, and we will build by going all the way down the leftmost branch first
# then go up from the last node to the 2nd to last level, build the next node on the last level, etc.
# so at any given time, we only need to store the current branch, and the index each level is on.
# i guess this is depth first building.


# broken - does too many permutations
# def search_tree(best_positions, position, num_legs, max_legs, option_legs, distribution, allow_uncapped_risk, num_to_return):
#     if num_legs > max_legs:
#         return
#
#     for option_leg in option_legs:
#         position.append(option_leg)
#
#         if is_position_allowed(position, allow_uncapped_risk):
#             metrics = option_position_metrics.compute_position_metrics(position, distribution)
#             option_position_results = OptionPositionResults(position, metrics)
#             update_best_positions_list(option_position_results, best_positions, num_to_return)
#
#         search_tree(best_positions, position, num_legs+1, max_legs, option_legs, distribution, allow_uncapped_risk, num_to_return)
#         position.remove(option_leg)


def update_best_positions_list(option_position_results, best_positions, max_list_size):
    """
    Sorts the best_positions list in descending order according to the expected value of the position.
    Drops the lowest valued entry if the list size exceeds max_list_size.  best_positions list will be mutated.
    :param option_position_results:
    :param best_positions:
    :param max_list_size:
    :return:
    """
    if len(best_positions) == max_list_size:
        if best_positions[max_list_size-1].metrics.ev > option_position_results.metrics.ev:
            # no need to modify the list, return
            return

    best_positions.append(option_position_results)
    best_positions.sort(key=lambda x: x.metrics.ev, reverse=True)
    if len(best_positions) > max_list_size:
        best_positions.pop()

    return best_positions


def is_position_allowed(option_position, allow_uncapped_risk):
    if allow_uncapped_risk:
        return True
    else:
        num_long_calls = count_long_calls(option_position)
        num_short_calls = count_short_calls(option_position)

        return num_long_calls >= num_short_calls


def count_long_calls(option_position):
    num_long_calls = 0
    for leg in option_position:
        if leg['direction'].lower() == 'long' and leg['option']['type'].lower() == 'call':
            num_long_calls += 1

    return num_long_calls


def count_short_calls(option_position):
    num_short_calls = 0
    for leg in option_position:
        if leg['direction'].lower() == 'short' and leg['option']['type'].lower() == 'call':
            num_short_calls += 1

    return num_short_calls
