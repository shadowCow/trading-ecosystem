__author__ = 'dwadeson'


class PositionMetrics:
    def __init__(self, ev, win_pct, rr, max_loss, max_gain):
        self.ev = ev
        self.win_pct = win_pct
        self.rr = rr
        self.max_loss = max_loss
        self.max_gain = max_gain

    def __str__(self):
        return '{ev: ' + str(self.ev) + ', win_pct: ' + str(self.win_pct) + ', rr: ' + str(self.rr) + ', max_loss: ' + str(self.max_loss) + ', max_gain: ' + str(self.max_gain) + '}'