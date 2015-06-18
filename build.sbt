name := "play-akka"

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "org.webjars" % "bootstrap" % "2.3.1",
  "org.webjars" % "flot" % "0.8.0",
  "com.typesafe.conductr" %% "play24-conductr-bundle-lib" % "0.13.0"
)

// Deploy settings
import ByteConversions._
BundleKeys.nrOfCpus := 1.0
BundleKeys.memory := 64.MiB
BundleKeys.diskSpace := 50.MiB
BundleKeys.roles := Set("stocks")
BundleKeys.endpoints := Map("stocks" -> Endpoint("http", services = Set(URI("http://:9000"))))
BundleKeys.startCommand += "-Dhttp.address=$STOCKS_BIND_IP -Dhttp.port=$STOCKS_BIND_PORT"
