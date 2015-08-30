__author__ = 'dwadeson'


# this represents an interval on the price axis where the value function of the options position is linear.
# that is, you can draw a straight line from the start of the interval to the end of the interval which
# will describe the value of the option position perfectly.
class OptionValueInterval:
    def __init__(self, start, end, value_at_start, value_at_end):
        self.start = start
        self.end = end
        self.value_at_start = value_at_start
        self.value_at_end = value_at_end
        self.slope = OptionValueInterval.compute_slope(self.start, self.value_at_start, self.end, self.value_at_end)
        self.zero_point = OptionValueInterval.compute_zero_point(self.start, self.value_at_start, self.end, self.value_at_end, self.slope)

    all_zero_point = 'all'

    @staticmethod
    def compute_slope(start, value_at_start, end, value_at_end):
        return (value_at_end - value_at_start) / (end - start)

    @staticmethod
    def compute_zero_point(start, value_at_start, end, value_at_end, slope):
        if value_at_start == 0 and value_at_end == 0:
            return OptionValueInterval.all_zero_point
        elif value_at_start == 0:
            return start
        elif value_at_end == 0:
            return end
        elif value_at_start > 0 and value_at_end > 0:
            return None
        elif value_at_start < 0 and value_at_end < 0:
            return None
        else:
            return start - (value_at_start / slope)

    def is_increasing_interval(self):
        return self.slope > 0 and not self.is_flat_interval()

    def is_decreasing_interval(self):
        return self.slope < 0 and not self.is_flat_interval()

    def is_flat_interval(self):
        """
        we can't be sure the slope will be exactly 0 due to floating point arithmetic.
        if we are within 4 decimal places, that is good enough.
        there are no real option positions that will have a value slope nearly that small.
        :return:
        """
        return -0.0001 < self.slope < 0.0001

    # returns the sub interval [a,b] over which the position value is > 0 or None if no such interval exists
    def get_positive_interval(self):
        if self.is_decreasing_interval() and self.value_at_start <= 0.0:
            return None
        elif self.is_increasing_interval() and self.value_at_end <= 0.0:
            return None
        elif self.is_flat_interval() and self.value_at_start <= 0.0:
            return None
        elif self.is_decreasing_interval():
            if self.zero_point is None:
                return [self.start, self.end]
            else:
                return [self.start, self.zero_point]
        elif self.is_increasing_interval():
            if self.zero_point is None:
                return [self.start, self.end]
            else:
                return [self.zero_point, self.end]
        elif self.is_flat_interval():
            return [self.start, self.end]

    # returns the sub interval [a,b] over which the position value is < 0 or None if no such interval exists
    def get_negative_interval(self):
        positive_interval = self.get_positive_interval()
        if positive_interval is None:
            if self.zero_point == OptionValueInterval.all_zero_point:
                return None
            else:
                return [self.start, self.end]
        elif positive_interval[0] == self.start:
            if positive_interval[1] == self.end:
                return None
            elif positive_interval[1] == self.zero_point:
                return [self.zero_point, self.end]
        else:
            # must start with zero_point
            return [self.start, self.zero_point]

    # since the value function on the interval is linear, the max value must be at an endpoint
    def get_max_value(self):
        if self.value_at_start >= self.value_at_end:
            return self.value_at_start
        else:
            return self.value_at_end

    # since the value function on the interval is linear, the min value must be at an endpoint
    def get_min_value(self):
        if self.value_at_start <= self.value_at_end:
            return self.value_at_start
        else:
            return self.value_at_end
