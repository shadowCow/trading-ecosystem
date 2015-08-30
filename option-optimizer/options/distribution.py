__author__ = 'dwadeson'


class Distribution:
    def __init__(self, domain_start, domain_end, cdf, pdf):
        self.domain_start = domain_start
        self.domain_end = domain_end
        self.cdf = cdf
        self.pdf = pdf
