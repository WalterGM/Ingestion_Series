ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.18"

val sparkVersion = "3.5.0"
val postgresVersion = "42.7.2"

lazy val root = (project in file("."))
  .settings(
    name := "Ingestion_Series"
  )
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
  "org.apache.spark" %% "spark-mllib" % sparkVersion % "provided",
  "com.typesafe" % "config" % "1.4.3",
  "com.google.cloud.bigdataoss" % "gcs-connector" % "hadoop3-2.2.20",
  "org.postgresql" % "postgresql" % postgresVersion,
  "com.google.cloud" % "google-cloud-secretmanager" % "2.37.0",
  "io.spray" %% "spray-json" % "1.3.6",
  "org.scalatest" %% "scalatest" % "3.2.18" % "test"
)

dependencyOverrides ++= Seq(
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.15.4"
)

ThisBuild / assemblyShadeRules := Seq(
  ShadeRule.rename("com.google.common.**" -> "my_conf.@1").inAll
)

ThisBuild / assemblyMergeStrategy := {
  case PathList("META-INF", xs @ _*) =>
    xs map { _.toLowerCase } match {
      case "manifest.mf" :: Nil | "index.list" :: Nil | "dependencies" :: Nil =>
        MergeStrategy.discard
      case ps @ x :: xs
          if ps.last.endsWith(".sf") || ps.last.endsWith(".dsa") =>
        MergeStrategy.discard
      case "plexus" :: xs =>
        MergeStrategy.discard
      case "services" :: xs =>
        MergeStrategy.filterDistinctLines
      case "spring.schemas" :: Nil | "spring.handlers" :: Nil =>
        MergeStrategy.filterDistinctLines
      case _ => MergeStrategy.first
    }
  case "application.conf" => MergeStrategy.concat
  case "reference.conf"   => MergeStrategy.concat
  case _                  => MergeStrategy.first
}