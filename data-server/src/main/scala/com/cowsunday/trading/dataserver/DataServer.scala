package com.cowsunday.trading.dataserver

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.{Directive0, Route}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.model.HttpMethods._
import spray.json.DefaultJsonProtocol._

import scala.concurrent.Future
import scala.io.StdIn


object DataServer extends CorsSupport {

  implicit val system = ActorSystem("data-actors")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  implicit val dataPointFormat = jsonFormat2(DataPoint)
  implicit val dataPointsFormat = jsonFormat1(DataPoints)


  def main(args: Array[String]): Unit = {

    val route: Route =
      get {
        path("latest-data") {
          val maybeData: Future[Option[DataPoints]] = DataRetriever.fetchData()

          onSuccess(maybeData) {
            case Some(data) => complete(data)
            case None => complete(StatusCodes.NotFound)
          }
        }
      }

    val bindingFuture = Http().bindAndHandle(corsHandler(route), "localhost", 8090)
    println(s"Data server online at localhost:8090\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}

trait CorsSupport {
  private def addAccessControlHeaders: Directive0 = {
    respondWithHeaders(
      `Access-Control-Allow-Origin`(HttpOrigin("http://localhost:3000"))
    )
  }

  private def preflightRequestHandler: Route = options {
    complete(HttpResponse(StatusCodes.OK).withHeaders(`Access-Control-Allow-Methods`(OPTIONS, POST, PUT, GET, DELETE)))
  }

  def corsHandler(r: Route) = addAccessControlHeaders {
    preflightRequestHandler ~ r
  }
}
