name := "spark-data-analysis"

version := "0.1.0"

scalaVersion := "2.10.5"

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.4.1" % "provided" exclude("org.slf4j", "slf4j-log4j12"),
  "org.apache.spark" %% "spark-mllib" % "1.3.0" % "provided",
  "org.slf4j" % "slf4j-api" % "1.7.12",
  "org.slf4j" % "slf4j-log4j12" % "1.7.12",
  "org.scalanlp" % "breeze_2.10" % "0.9",
  "org.specs2" %% "specs2" % "2.4.17" % "test"
)

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
  {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case x => MergeStrategy.first
  }
}