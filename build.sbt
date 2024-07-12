/*
ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.18"

lazy val root = (project in file("."))
  .settings(
    name := "HelloSpark"
  )

*/
name := "HelloSpark"
organization := "akash.spark"
version := "0.1"
scalaVersion := "2.12.18"
autoScalaLibrary := false

val sparkVersion="3.5.1"

val sparkDependencies= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion
)

val testDependencies= Seq(
  "org.scalatest" %% "scalatest" % "3.0.8" % Test
)

libraryDependencies ++= sparkDependencies ++ testDependencies
