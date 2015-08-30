__author__ = 'dwadeson'


def call(strike, price):
    return create_option('Call', strike, price)


def put(strike, price):
    return create_option('Put', strike, price)


def long_call(strike, price):
    return create_option_leg('Call', strike, price, 'Long')


def short_call(strike, price):
    return create_option_leg('Call', strike, price, 'Short')


def long_put(strike, price):
    return create_option_leg('Put', strike, price, 'Long')


def short_put(strike, price):
    return create_option_leg('Put', strike, price, 'Short')


def create_option_leg(option_type, strike, price, direction):
    return {
        'option': {
            'type': option_type,
            'strike': strike,
            'price': price
        },
        'direction': direction
    }


def create_option(option_type, strike, price):
    return {
        'type': option_type,
        'strike': strike,
        'price': price
    }
