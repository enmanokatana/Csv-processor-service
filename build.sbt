import scala.collection.immutable.Seq


ThisBuild / version := "0.1.0-SNAPSHOT"


ThisBuild / scalaVersion := "2.13.15"


val akkaVersion = "2.8.0"
val akkaHttpVersion = "10.5.0"


lazy val root = (project in file("."))
  .settings(
    name := "Vsc", 
    libraryDependencies ++= Seq(
      
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
      
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      
      "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
      
      "com.typesafe.play" %% "play-json" % "2.9.4",
      
      "com.github.tototoshi" %% "scala-csv" % "1.3.10",
      
      "ch.qos.logback" % "logback-classic" % "1.4.7",
      
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
      
      "org.scalatest" %% "scalatest" % "3.2.15" % Test
    )
  )