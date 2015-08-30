__author__ = 'dwadeson'

from options.position_metrics import PositionMetrics


class OptionPositionResults:
    def __init__(self, option_position, metrics):
        if not (type(metrics) is PositionMetrics):
            raise TypeError('metrics is not a PositionMetrics object')

        self.option_position = option_position
        self.metrics = metrics

    def __str__(self):
        return '{option_position: ' + self.option_position.__str__() + ', metrics: ' + self.metrics.__str__() + '}'