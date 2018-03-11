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

case class DataPoint(x: Int, y: Int)
case class DataPoints(dataPoints: Seq[DataPoint])

object DataServer extends CorsSupport {

  implicit val system = ActorSystem("data-actors")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  implicit val dataPointFormat = jsonFormat2(DataPoint)
  implicit val dataPointsFormat = jsonFormat1(DataPoints)

  // (fake) async data grab
  def fetchData(): Future[Option[DataPoints]] = Future {

    val dummyData = (0 until 30).map(i => {
      val x = math.random * 100
      val y = math.random * 100

      DataPoint(x.toInt,y.toInt)
    })

    Option(DataPoints(dummyData))
  }

  def main(args: Array[String]): Unit = {


    val route: Route =
      get {
        path("latest-data") {
          val maybeData: Future[Option[DataPoints]] = fetchData()

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
