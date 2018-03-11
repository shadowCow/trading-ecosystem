package com.cowsunday.trading.ml.ondemand

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.model.headers.HttpOrigin
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.server.{Directive0, Route}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.cowsunday.trading.ml.transform.ColumnProducers
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import spray.json.DefaultJsonProtocol._

import scala.concurrent.Future

case class AvailableTransforms(names: Seq[String])
case class TransformCommand(feature: String, metric: String)
case class TransformResult(didSucceed: Boolean, error: Option[String] = None)

object TransformServer extends CorsSupport {

  implicit val system = ActorSystem("transform-actors")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  implicit val availableTransformsFormat = jsonFormat1(AvailableTransforms)
  implicit val transformCommandFormat = jsonFormat2(TransformCommand)
  implicit val transformResultFormat = jsonFormat2(TransformResult)


  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.ERROR)
    Logger.getLogger("akka").setLevel(Level.ERROR)

    implicit val sparkSession = SparkSession.builder()
      .master("local")
      .appName("Trading Algo Pipeline")
      .getOrCreate()

    val route: Route =
      get {
        path("available-transforms") {
          val responseData = AvailableTransforms(ColumnProducers.transforms.map(_.name))

          complete(responseData)
        }
      } ~
        post {
          path("perform-transform") {
            entity(as[TransformCommand]) { command =>
              val featureTransform = ColumnProducers.transforms.find(_.name == command.feature)
              val metricTransform = ColumnProducers.transforms.find(_.name == command.metric)

              if (featureTransform.isDefined && metricTransform.isDefined) {
                val result = Future(
                  ResultsGenerator.transformAndSave(
                    "src/main/resources/sp103_daily.csv",
                    featureTransform.get,
                    metricTransform.get
                  )
                )

                onSuccess(result) { r =>
                  complete(TransformResult(r))
                }
              } else {
                val missingFeatureMsg = if (featureTransform.isEmpty) command.feature else ""
                val missingMetricMsg = if (metricTransform.isEmpty) command.metric else ""

                val msg = s"Unknown transforms: [$missingFeatureMsg,$missingMetricMsg]"
                complete(TransformResult(false, Option(msg)))
              }

            }
          }
        }

    val bindingFuture = Http().bindAndHandle(corsHandler(route), "localhost", 8091)
    println(s"Spark transform server online at localhost:8091...")
    while(true) {}
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => {
        system.terminate()
        sparkSession.stop()
      })
  }
}


trait CorsSupport {
  private def addAccessControlHeaders: Directive0 = {
    respondWithHeaders(
      `Access-Control-Allow-Origin`(HttpOrigin("http://localhost:3000")),
      `Access-Control-Allow-Headers`("Content-Type")
    )
  }

  private def preflightRequestHandler: Route = options {
    complete(HttpResponse(StatusCodes.OK).withHeaders(`Access-Control-Allow-Methods`(OPTIONS, POST, PUT, GET, DELETE)))
  }

  def corsHandler(r: Route) = addAccessControlHeaders {
    preflightRequestHandler ~ r
  }
}
