name := """inmobi-api"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "com.amazonaws" % "aws-java-sdk" % "1.10.2",
  "com.amazonaws" % "aws-java-sdk-core" % "1.9.6",
  "org.mongodb.morphia" % "morphia" % "1.1.0"
)

libraryDependencies += "commons-io" % "commons-io" % "2.4"


// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
