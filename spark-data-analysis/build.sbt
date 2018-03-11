name := "spark-data-analysis"

version := "0.2.0"

scalaVersion := "2.11.8"

organization := "hai"

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.2.0" % "provided" exclude("org.slf4j", "slf4j-log4j12"),
  "org.apache.spark" %% "spark-mllib" % "2.2.0",
  "org.slf4j" % "slf4j-api" % "1.7.12",
  "org.slf4j" % "slf4j-log4j12" % "1.7.12",
  "org.scalanlp" %% "breeze" % "0.10",
  "org.specs2" %% "specs2" % "3.7" % "test",
  "com.typesafe.akka" %% "akka-http"   % "10.1.0",
  "com.typesafe.akka" %% "akka-stream" % "2.5.11",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.0",
  "io.spray" %%  "spray-json" % "1.3.3"
)

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
  {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case x => MergeStrategy.first
  }
}


addCommandAlias("onDemand", "runMain com.cowsunday.trading.ml.ondemand.OnDemandAnalysis")
addCommandAlias("transformServer", "runMain com.cowsunday.trading.ml.ondemand.TransformServer")

mainClass in reStart := Some("com.cowsunday.trading.ml.ondemand.TransformServer")