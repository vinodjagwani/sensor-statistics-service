scalaVersion := "2.13.10"
name := "sensor-statistics-service"
organization := "com.luxoft.sensor.statistics"
idePackagePrefix := Some("com.luxoft.sensor.statistics")
version := "0.1"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio-streams"  % "1.0.16",
  "dev.zio" %% "zio-test"     % "1.0.16"  % "test",
  "dev.zio" %% "zio-test-sbt" % "1.0.16"  % "test",
  "org.scalatest" %% "scalatest" % "3.2.15" % Test
)
