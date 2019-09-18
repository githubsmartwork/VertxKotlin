/*
package com.coats.d3

import com.coats.d3.dao.PersonDao
import io.vertx.core.Handler
import io.vertx.core.Vertx

import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import kotlinx.serialization.*
import kotlinx.serialization.json.*




//@ImplicitReflectionSerializer
fun main(args: Array<String>) {
  println("Hello, World")
  val vertx = Vertx.vertx()
  val httpServer = vertx.createHttpServer()
  val dao = PersonDao()
  val router = Router.router(vertx)
 // val router = createRouter()
  val json = Json(JsonConfiguration.Stable)
  router.route().handler(BodyHandler.create())
  // router.route().handler(ResponseContentTypeHandler.create())


 // router.get("/")
  val handlerRoot  = Handler<RoutingContext> {
    routingContext ->
    val response = routingContext.response()
    response.putHeader("content-type", "text/plain")
      .setChunked(true)
      .write("Hi kotlin\n")
      .end("we are on")
  }


  router.get("/person")
    .handler { routingContext ->
      var response = routingContext.response()
      response.putHeader("content-type", "application/json")
        .setChunked(true)
        // .write(Json.encodePrettily(Person(1 ,"Satheesh",23,"India")))
        .write(json.stringify(Person.serializer().list,dao.fetchPerson()))
        .setStatusCode(200)
        .end()
    }

  router.post("/personSave").consumes("application/json").
    produces("application/json").
    handler { routingContext ->
      val response = routingContext.response()
      val personString:String = routingContext.bodyAsString
      try {

        val personObject = Json.parse(Person.serializer(), personString)
        var person : Person = dao.addPerson(personObject)
        val personJson=Json.stringify(Person.serializer(), person)
        if((personString.contains("country" ,true))){

        }
        else{
          routingContext.fail(500,RuntimeException("Sorry! Something went wrong") );
        }
        if((personString.contains("pid" ,true))){
          response.statusCode = 400
          response.putHeader("content-type", "application/json")
            .setChunked(true)
            .write("Person Saved Successfully\n")
            .end()
        }
        else {
          response.putHeader("content-type", "application/json")
          response.statusCode = 201
          response.setChunked(true).write("Person Saved Successfully\n").end(personJson)
        }
      }
      catch (e:Throwable){
        println(e.message)
        //response.statusCode=500
        routingContext.fail(500)
      }



    }

  router.get("/person/:id")
    .handler { routingContext ->

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
          .write(Json.stringify(Person.serializer().list,personObj))
          .setStatusCode(200)
          .end()
      }


      router.get("/person/:id")
        .failureHandler { failureRoutingContext ->

          val statusCode = failureRoutingContext.statusCode()

          var response = failureRoutingContext.response()
          response.setStatusCode(statusCode).end("Sorry! Person ID not found")

        }

      router.post("/personSave")
        .failureHandler { failureRoutingContext ->

          val statusCode = failureRoutingContext.statusCode()

          var response = failureRoutingContext.response()
          response.setStatusCode(statusCode).end("Sorry! Something went wrong")

        }
    }

  httpServer
    .requestHandler(router::accept)
    .listen(8081);
}

*/
/*
fun createRouter() = Router.router(vertx).apply {
  get("/").handler(handlerRoot)
  get("/person").handler(handlerPerson)
  get("/person/:id").handler(handlerPersonId)
  post("/personSave").handler(handlerPersonSave)
}
*//*



@Serializable
data class Person(var pid:Int=0 ,var name:String = "",var age:Int,var country:String)
@Serializable
data class Country(val name: String, val code: String)

*/
