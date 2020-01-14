name := "TestEx"

version := "0.1"

scalaVersion := "2.13.1"

val akkaVersion = "2.5.23"
val akkaHttpVersion = "10.1.11"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "org.typelevel" %% "cats-core" % "2.1.0"
)

