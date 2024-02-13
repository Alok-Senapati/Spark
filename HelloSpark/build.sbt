ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.12.18"
ThisBuild / autoScalaLibrary := false
val sparkVersion = "3.5.0"

val sparkDependencies = Seq(
  "org.apache.spark" %% "spark-core" % "3.5.0",
  "org.apache.spark" %% "spark-sql" % "3.5.0"
)

val testDependencies = Seq(
  "org.scalatest" %% "scalatest" % "3.2.18" % Test
)

lazy val root = (project in file("."))
  .settings(
    name := "HelloSpark"
  )

libraryDependencies ++= sparkDependencies ++ testDependencies
