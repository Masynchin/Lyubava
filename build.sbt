ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"

libraryDependencies += "org.typelevel" %% "cats-core" % "2.10.0"
libraryDependencies += "org.typelevel" %% "cats-effect" % "3.5.2"
libraryDependencies += "com.monovore" %% "decline-effect" % "2.4.1"
libraryDependencies += "com.lihaoyi" %% "fansi" % "0.4.0"

lazy val root = (project in file("."))
  .settings(
    name := "lyubava"
  )
