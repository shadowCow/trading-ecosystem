name := "data-server"

version := "1.0"

scalaVersion := "2.12.4"

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http"   % "10.1.0",
  "com.typesafe.akka" %% "akka-stream" % "2.5.11",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.0",
  "io.spray" %%  "spray-json" % "1.3.3"
)

addCommandAlias("serveData", "sbt runMain com.cowsunday.trading.dataserver.DataServer")
