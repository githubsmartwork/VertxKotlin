# VertxKotlin

The challenge is to create a maven project which builds a fat jar that, when run, starts an HTTP server on port 8080.  This server should provide a /person endpoint that responds as follows:

1.  POST /person, accepting content-type application/JSON: should parse the JSON body into a Person object. 
If the body contains an id field return status 400; otherwise allocate a unique numeric id to the person and save the object in an in-memory collection (e.g. a Map or List of some kind).  The endpoint should return status 201 and a JSON body containing the Person object including the newly allocated id field.
2.  GET /person  This should return all of the stored person objects, including their id fields
3.  GET /person/:id  This should return a JSON body of the corresponding person object, if found (with a 200 status) or 404 if the person is not found
4.  If an error occurs during handling of a request, handle this by returning a failure response in JSON.

Use a kotlin data class for Person

Use kotlinx-serialisation to convert from Person to JSON and vice versa

Use a Vertx Router to route the different requests to different handlers

Use a Vertx BodyHandler to simplify dealing with request bodies

Use a Vertx FailureHandler to direct errors to a handler
