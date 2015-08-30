#!flask/bin/python
__author__ = 'dwadeson'
from flask import Flask, jsonify, request, make_response, url_for, abort


app = Flask(__name__)


# example scenario data structure:
# {
#   options: [
#               {
#                   type: 'Call',
#                   strike: 105,
#                   price: 3.50
#               },
#               {
#                   type: 'Put',
#                   strike: 90,
#                   price: 1.25
#               },
#               ...more options...
#   ],
#   distribution: {
#                   type: 'Uniform',
#                   parameters: {
#                                   a: 10,
#                                   b: 25
#                               }
#   },
#   allow_uncapped_risk: false,
#   max_legs: 4
# }
@app.route('/options-analysis/compute-best-positions', methods=['POST'])
def compute_best_positions(scenario):
    abort(503)


@app.route('/')
def index():
    return "Hello, World!"


@app.errorhandler(503)
def service_unavailable(error):
    return make_response(jsonify({'error': 'Service unavailable'}), 503)

if __name__ == '__main__':
    app.run(debug=True)


