name := "akka-http-rest-api"

version := "1.0"

scalaVersion := "2.11.7"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= {
  val akkaV = "2.3.13"
  val akkaStreamV = "1.0"
  val slickV = "3.0.3"
  val postgresV = "9.4-1202-jdbc42"
  val rediscalaV = "1.4.0"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-stream-experimental" % akkaStreamV,
    "com.typesafe.akka" %% "akka-http-core-experimental" % akkaStreamV,
    "com.typesafe.akka" %% "akka-http-experimental" % akkaStreamV,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaStreamV,
    "com.typesafe.slick" %% "slick" % slickV,
    "org.postgresql" % "postgresql" % postgresV,
    "com.etaty.rediscala" %% "rediscala" % rediscalaV,
    "joda-time" % "joda-time" % "2.8.2"
  )
}

seq(flywaySettings: _*)

flywayUrl := "jdbc:postgresql://localhost:5432/rest_api_app"

flywayUser := "postgres"

flywayPassword := "postgres"