__author__ = 'dwadeson'

from scipy import integrate
from options import option_value_interval
from options.position_metrics import PositionMetrics


# given an option position and the distribution of prices for the underlying asset,
# this method will compute the following metrics:

# Expected Value (ev)
# Maximum Loss possible (max_loss)
# Probability that the position gains value (win_pct)
# Reward/Risk ratio (rr)
#
# an option position is a set of option legs (e.g. Long option1, short option2, long option3),
def compute_position_metrics(option_position, distribution):
    option_value_intervals = get_option_value_intervals(option_position, distribution)
    max_loss = compute_max_loss(option_value_intervals)
    max_gain = compute_max_gain(option_value_intervals)
    win_pct = compute_win_pct(option_value_intervals, distribution.cdf)

    win_intervals = compute_win_intervals(option_value_intervals)
    loss_intervals = compute_loss_intervals(option_value_intervals)
    expected_gain = cumulative_expectation(win_intervals, option_position, distribution.pdf)
    # expected_loss should be a positive number when passed into the rr ratio function
    expected_loss = (-1) * cumulative_expectation(loss_intervals, option_position, distribution.pdf)
    rr = compute_reward_risk_ratio(expected_gain, expected_loss)
    ev = compute_expected_value(rr, win_pct)

    metrics = PositionMetrics(ev, win_pct, rr, max_loss, max_gain)

    return metrics


def get_option_value_intervals(option_position, distribution):
    strike_prices = list(map(lambda leg: leg['option']['strike'], option_position))
    strike_prices.sort()

    option_value_intervals = []
    interval1 = option_value_interval.OptionValueInterval(distribution.domain_start,
                                                          strike_prices[0],
                                                          position_value_function(distribution.domain_start, option_position),
                                                          position_value_function(strike_prices[0], option_position))
    option_value_intervals.append(interval1)

    strike_prices.append(distribution.domain_end)
    for i in range(0, len(strike_prices)-1):
        if strike_prices[i] != strike_prices[i+1]:
            # if the options have the same strikes, the interval will be non-existent.
            # so only make an interval if they don't
            interval = option_value_interval.OptionValueInterval(strike_prices[i],
                                                                 strike_prices[i+1],
                                                                 position_value_function(strike_prices[i], option_position),
                                                                 position_value_function(strike_prices[i+1], option_position))
            option_value_intervals.append(interval)

    return option_value_intervals


# this method will return the domain intervals over which the position has a net gain
def compute_win_intervals(option_value_intervals):
    win_intervals = []
    for interval in option_value_intervals:
        positive_sub_interval = interval.get_positive_interval()
        if positive_sub_interval is not None:
            win_intervals.append(positive_sub_interval)

    return win_intervals


# this method will return the domain intervals over which the position has a net loss
def compute_loss_intervals(option_value_intervals):
    loss_intervals = []
    for interval in option_value_intervals:
        negative_sub_interval = interval.get_negative_interval()
        if negative_sub_interval is not None:
            loss_intervals.append(negative_sub_interval)

    return loss_intervals


# this method will return the probability that the position yields a net gain
#
# intervals is a list of segments [a,b] where the option value function can be described by a linear function
# ax + b
def compute_win_pct(option_value_intervals, cdf):
    total_win_pct = 0.0
    for interval in option_value_intervals:
        positive_sub_interval = interval.get_positive_interval()
        if positive_sub_interval is not None:
            total_win_pct += cdf(positive_sub_interval[1]) - cdf(positive_sub_interval[0])

    return total_win_pct


# computes the maximum loss possible for the option position
# the maximum loss is defined as the most negative value of the domain for which the position yields a net loss
# if the position has a possible loss, then the returned number should be negative.
# if the position's least value is 0, then the returned number should be 0
# if the position's least value is positive, then the returned number should be positive
def compute_max_loss(option_value_intervals):
    max_loss = float('inf')
    for interval in option_value_intervals:
        interval_min_value = interval.get_min_value()
        if interval_min_value < max_loss:
            max_loss = interval_min_value

    return max_loss


def compute_max_gain(option_value_intervals):
    max_gain = -float('inf')
    for interval in option_value_intervals:
        interval_max_value = interval.get_max_value()
        if interval_max_value > max_gain:
            max_gain = interval_max_value

    return max_gain


# computes the expectation of the options with the given asset price pdf for each interval.
# then sums up the results from each interval and returns the cumulative value.
# intervals is a list like [[a,b],[c,d],...[x,y]]
def cumulative_expectation(intervals, option_position, pdf):
    my_sum = 0.0
    for interval in intervals:
        integration_result = integrate.quad(lambda x: expected_value_function(x, option_position, pdf),
                                            interval[0],
                                            interval[1])
        my_sum += integration_result[0]

    return my_sum


# defines the function inside the integral when taking expectation
# this is passed into the scipy integrate methods for numerical integration
# this is NOT expectation.
# Given E[g(X)] = integralOf(g(x) * f(x) dx), this function is g(x) * f(x)
def expected_value_function(asset_price, option_position, pdf):
    return position_value_function(asset_price, option_position) * pdf(asset_price)


def position_value_function(asset_price, option_position):
    my_sum = 0.0
    for option_leg in option_position:
        option = option_leg['option']
        if option['type'] == 'Call':
            my_sum += (max(0, asset_price - option['strike']) - option['price']) \
                * get_position_multiplier(option_leg['direction'])
        elif option['type'] == 'Put':
            my_sum += (max(0, option['strike'] - asset_price) - option['price']) \
                * get_position_multiplier(option_leg['direction'])

    return my_sum;


def compute_reward_risk_ratio(expected_gain, expected_loss):
    if expected_gain < 0.0 or expected_loss < 0.0:
        raise ValueError('expected_gain and expected_loss must be >= 0')
    elif expected_loss == 0.0 and expected_gain == 0.0:
        return float("nan")
    elif expected_loss == 0.0:
        return float("inf")
    else:
        return expected_gain / expected_loss


def compute_expected_value(reward_risk_ratio, win_pct):
    return reward_risk_ratio * win_pct - (1 - win_pct)


def get_position_multiplier(direction):
    if direction.lower() == 'long':
        return 1
    elif direction.lower() == 'short':
        return -1
    else:
        return 0