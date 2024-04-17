ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.18"

val sparkVersion = "3.5.0"
val postgresVersion = "42.7.2"


lazy val root = (project in file("."))
  .settings(
    name := "Spark_POC"
  )
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-mllib" % sparkVersion,
  "com.typesafe" % "config" % "1.4.3",
  "com.google.cloud.bigdataoss" % "gcs-connector" % "hadoop3-2.2.20",
  "commons-io" % "commons-io" % "2.15.1",
  "org.postgresql" % "postgresql" % postgresVersion,
  "com.google.cloud" % "google-cloud-secretmanager" % "2.37.0",
  "io.spray" %%  "spray-json" % "1.3.6",
  "com.google.cloud" % "google-cloud-bigquery" % "2.38.1",
  "com.google.cloud.spark" %% "spark-bigquery-with-dependencies" % "0.37.0"
)

dependencyOverrides ++= Seq(
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.15.4"
)

ThisBuild / assemblyMergeStrategy := {
  case PathList("javax", "servlet", xs@_*) => MergeStrategy.first
  case PathList(ps@_*) if ps.last endsWith ".html" => MergeStrategy.first
  case "application.conf" => MergeStrategy.concat
  case "unwanted.txt" => MergeStrategy.discard
  case x =>
    val oldStrategy = (ThisBuild / assemblyMergeStrategy).value
    oldStrategy(x)
}