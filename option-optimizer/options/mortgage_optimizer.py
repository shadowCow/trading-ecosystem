__author__ = 'dwadeson'

from scipy import optimize
import math


def periodic_payment_function(c, i):
    return lambda x: (c - x[0]) * (i + (i / (math.pow(i+1, x[1]) - 1)))


def optimize_mortgage(c, i, t, maxPeriodicPayment, minimumDownPayment):
    """
    the vector x used below represents the down payment (x[0]) and the number of periods of the loan (x[1])

    we multiply by -1 in our function f because we want to maximize the function
    :param c: cost of the property
    :param i: interest rate per period
    :param t: number of periods the property will be held
    :return: vector with the down payment and number of periods which yields an optimal return
    """
    downPaymentRatio = lambda x: (c - x[0]) / x[0]
    f1 = lambda x: i + (i / (math.pow(1+i, x[1]) - 1))
    f2 = lambda x: (math.pow(1 + i, t) - 1) / i
    f3 = lambda x: 1 - math.pow(1 + i, t) + f1(x) * f2(x)
    f4 = lambda x: (downPaymentRatio(x) * f3(x)) + 1
    f = lambda x: -1 * math.pow(f4(x), (1/t))

    # constraints
    # minimumDownPayment < d <= c (fudge by having 1 <= d <= c)
    # t < n <= 360 (fudge with t+1 <= n <= 360)
    # 0 < A <= maxPeriodicPayment (this is the constraint on the size of the periodic payment)
    A = periodic_payment_function(c, i)
    cons = ({
        'type' : 'ineq',
        'fun' : lambda x: x[0] - minimumDownPayment
    }, {
        'type' : 'ineq',
        'fun' : lambda x: c - x[0]
    }, {
        'type' : 'ineq',
        'fun' : lambda x: x[1] - t - 1
    }, {
        'type' : 'ineq',
        'fun' : lambda x: 360 - x[1]
    }, {
        'type' : 'ineq',
        'fun' : lambda x: A(x)
    }, {
        'type' : 'ineq',
        'fun' : lambda x: maxPeriodicPayment - A(x)
    })
    my_answer = optimize.minimize(f, [1.5, 1.5], constraints=cons, options={'disp': True})

    return my_answer


def optimize_mortgage_with_extra_payments(c, i, t, max_required_periodic_payment, max_total_periodic_payment, minimum_down_payment):
    """
    the vector x used below represents:
     the down payment (x[0])
     the number of periods of the loan (x[1])
     the optional periodic prepayment size (x[2])

    we multiply by -1 in our function f because we want to maximize the function
    :param c: cost of the property
    :param i: interest rate per period
    :param t: number of periods the property will be held
    :param max_required_periodic_payment: the maximum periodic payment required by the terms of the loan
    :param max_total_periodic_payment: the maximum periodic payment including optional extra payments
    :return: vector with the down payment and number of periods and period prepayment size which yields an optimal return
    """

    A = periodic_payment_function(c, i)
    M = lambda x: A(x) + x[2]
    f1 = lambda x: M(x) * ((math.pow(1+i, t) - 1) / i)
    f2 = lambda x: (c - x[0]) - (c - x[0]) * math.pow(1 + i, t) + f1(x)
    f3 = lambda x: f2(x) / x[0]
    # multiply by -1 because we want to maximize but we are using a minimization routine
    f = lambda x: -1 * math.pow(f3(x) + 1, (1/t))

    # constraints
    # minimumDownPayment < d <= c (fudge by having 1 <= d <= c)
    # t < n <= 360 (fudge with t+1 <= n <= 360)
    # 0 < A <= maxPeriodicPayment (this is the constraint on the size of the periodic payment)

    cons = ({
        'type' : 'ineq',
        'fun' : lambda x: x[0] - minimum_down_payment
    }, {
        'type' : 'ineq',
        'fun' : lambda x: c - x[0]
    }, {
        'type' : 'ineq',
        'fun' : lambda x: x[1] - t - 1
    }, {
        'type' : 'ineq',
        'fun' : lambda x: 360 - x[1]
    }, {
        'type' : 'ineq',
        'fun' : lambda x: A(x)
    }, {
        'type' : 'ineq',
        'fun' : lambda x: max_required_periodic_payment - A(x)
    }, {
        'type' : 'ineq',
        'fun' : lambda x: max_total_periodic_payment
    }, {
        'type' : 'ineq',
        'fun' : lambda x: max_total_periodic_payment - A(x) - x[2]
    })

    best_answer = None
    starting_vectors = [[1.5, 1.5, 1.5], [1.5, 1.5, 200], [1.5, 1.5, 300], [1.5, 1.5, 400], [1.5, 1.5, 499], [1.5, 1.5, 999]]
    for starting_vector in starting_vectors:
        my_answer = optimize.minimize(f, starting_vector, constraints=cons, options={'disp': True})
        if best_answer is None:
            best_answer = my_answer
        elif my_answer.fun < best_answer.fun:
            best_answer = my_answer

    return best_answer


if __name__ == '__main__':
    c = 150000
    i = 0.00367
    t = 60

    answer = optimize_mortgage_with_extra_payments(c, i, t, 1000, 2000, 10000)

    periodic_payment = periodic_payment_function(c, i)(answer.x)
    print("results: d = ", answer.x[0], ", n = ", answer.x[1], ", obj. value = ", -1*answer.fun,
          ", monthly payment (A)= ", periodic_payment,
          ", monthly including prepayment (M)= ", (periodic_payment + answer.x[2]))
