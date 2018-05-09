import pymongo
from pymongo import MongoClient

def connectToMongo():
    """Establish a connection to the 'callReports' database in the 'callReports'
    collection in the MongoDB instance running at localhost:27017.

    Returns:
        A pymongo.collection.Collection object that can be used to query
        or modify the underlying database collection.
    """
    databaseName = 'callReports'
    collectionName = 'callReports'

    client = MongoClient('localhost', 27017)
    db = client[databaseName]
    collection = db[collectionName]
    print("Connected to database {}".format(databaseName))
    return collection
