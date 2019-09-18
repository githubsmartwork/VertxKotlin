package com.coats.d3

import com.coats.d3.dao.PersonDao
import io.vertx.core.AbstractVerticle
import io.vertx.core.Handler
import io.vertx.core.Promise
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.list

class MainVerticle : AbstractVerticle() {

  override fun start(startPromise: Promise<Void>) {
   val router = createRouter()

   // router.route().handler(ResponseContentTypeHandler.create())
    vertx
      .createHttpServer()
      .requestHandler(router)
      .listen(8080) { http ->
        if (http.succeeded()) {
          startPromise.complete()
          println("HTTP server started on port 8080")
        } else {
          startPromise.fail(http.cause())
        }
      }

  }

  private val dao = PersonDao()
  private val json = Json(JsonConfiguration.Stable)



  private val handlerRoot = Handler<RoutingContext> { routingContext ->
    val response = routingContext.response()
    response.putHeader("content-type", "text/plain")
      .setChunked(true)
      .write("Hi kotlin\n")
      .end("we are on")
  }


  private val handlerPerson = Handler<RoutingContext> { routingContext ->
    val response = routingContext.response()
    response.putHeader("content-type", "application/json")
      .setChunked(true)
      // .write(Json.encodePrettily(Person(1 ,"Satheesh",23,"India")))
      .write(json.stringify(Person.serializer().list, dao.fetchPersons()))
      .setStatusCode(200)
      .end()
  }

  private val handlerPersonSave = Handler<RoutingContext> { routingContext ->
    println("Inside handlerPersonSave")

    try {

      val response = routingContext.response()
      val personString:String = routingContext.bodyAsString
      //println("personString --> ${personString}")
      val personObject = json.parse(Person.serializer(), personString)
      //println("personObject --> ${personObject}")
      //println("personObject[0] --> ${personObject[0]}")
      val personlist=dao.fetchPersons()
      personlist.map { it-> if (it.pid == personObject.pid)
        routingContext.fail(406, RuntimeException("Sorry! Person Id already exits"))
      }
      val person: Person = dao.addPerson(personObject)
      val personJson = json.stringify(Person.serializer(), person)
    /*  if ((personString.contains("country", true))) {

      } else {
        routingContext.fail(500, RuntimeException("Sorry! Something went wrong"))
      }*/

      if ("country" !in personString){
        routingContext.fail(500, RuntimeException("Sorry! Something went wrong"))
      }
      if ("name" !in personString){
        routingContext.fail(500, RuntimeException("Sorry! Something went wrong"))
      }
      if ((personString.contains("pid", true))) {

        response.statusCode = 400
        response.putHeader("content-type", "application/json")
          .setChunked(true)
          .write("Person Saved Successfully\n")
          .end()
      } else {
        response.putHeader("content-type", "application/json")
        response.statusCode = 201
        response.setChunked(true).write(" Kudos !!! Person Saved Successfully\n").end(personJson)
      }
    } catch (e: Throwable) {
      println("Inside catch ")
      println(e.message)
      //response.statusCode=500
      routingContext.fail(500)
    }


  }
  private val handlerPersonId= Handler<RoutingContext> { routingContext ->
    val request = routingContext.request()
    val pid = request.getParam("id").toInt()
    val response = routingContext.response()
    val personObj = dao.fetchPerson(pid)
    if (personObj.isEmpty()) {
      // response.setStatusCode(404).end(" Person ID not found")
      routingContext.fail(404)

    } else {
      response.putHeader("content-type", "application/json")
        .setChunked(true)
        .write(json.stringify(Person.serializer().list, personObj))
        .setStatusCode(200)
        .end()
    }

  }

  private fun createRouter() = Router.router(vertx).apply {
    route().handler(BodyHandler.create())
    get("/").handler(handlerRoot)
    get("/person").consumes("application/json").produces("application/json").handler(handlerPerson)
    get("/person/:id").consumes("application/json").produces("application/json").handler(handlerPersonId)
    post("/personSave").consumes("application/json").produces("application/json").handler(handlerPersonSave)
    post("/personSave").consumes("application/json").produces("application/json").failureHandler { failureRoutingContext ->
      val statusCode = failureRoutingContext.statusCode()

      var response = failureRoutingContext.response()
      if(statusCode==500) {
       var er= ErrorResponse("Request Parse Error",500,"Sorry! Request tags are not good. Please check the request")
        val erString = json.stringify(ErrorResponse.serializer(), er)
        response.setStatusCode(statusCode).end(erString)
      }
      else if(statusCode==406){
        var er= ErrorResponse("Business error",406,"Sorry!  Person Id already exits ..Please try with new ID")
        val erString = json.stringify(ErrorResponse.serializer(), er)
        response.setStatusCode(statusCode).end(erString)
      }
    }
    get("/person/:id").consumes("application/json").produces("application/json").failureHandler{ failureRoutingContext ->
      val statusCode = failureRoutingContext.statusCode()
      var response = failureRoutingContext.response()

      var er= ErrorResponse("Not found Error",404,"Sorry! Person ID not found")
      val erString = json.stringify(ErrorResponse.serializer(), er)
      response.setStatusCode(statusCode).end(erString)
    }
  }
}
@Serializable
data class Person(var pid:Int=0 ,var name:String ,var age:Int,var country:Country)
@Serializable
data class Country(val name: String, val code: String)
@Serializable
data class ErrorResponse(var ExceptionName: String, val ExceptionCode :Int,var Description:String )
