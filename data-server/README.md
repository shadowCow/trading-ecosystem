# DataServer

This project is a companion to `spark-data-analysis`.  
The intent is...
- `spark-data-analysis` will be used to produce data representing analysis results.
- `data-server` will provide an API to access those results from a web client.
- `visualization` is the web client that renders those results.

`spark-data-analysis` and `data-server` will share protocols/conventions for data storage locations.

Eventually, this stack should be highly interactive, where the web client can be used to define analysis routines, invoke them, and show results.
