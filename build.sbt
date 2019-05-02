organization  := "io.sqooba"
scalaVersion  := "2.13.0-M5"
name          := "sq-conf"
description   := "Unified configuration interface."
homepage      := Some(url("https://github.com/Sqooba/sq-conf"))
licenses      := Seq("Apache 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"))

crossScalaVersions := Seq("2.13.0-M5", "2.12.8", "2.11.12")

libraryDependencies ++= Seq(
  "com.typesafe"                %   "config"                  % "1.3.3",
  "com.typesafe.scala-logging"  %%  "scala-logging"           % "3.9.0",
  "ch.qos.logback"              %   "logback-classic"         % "1.2.3"             % Test,
  "org.scalatest"               %%  "scalatest"               % "3.0.7"             % Test,
  "org.mockito"                 %   "mockito-all"             % "1.10.19"           % Test
)

excludeDependencies ++= Seq("org.slf4j" % "slf4j-log4j12", "log4j" % "log4j")

coverageHighlighting    := true
publishMavenStyle       := true
publishArtifact in Test := false
pomIncludeRepository    := { _ => false }
parallelExecution in Test := false

publishTo               := Some(sonatypeDefaultResolver.value)

scmInfo := Some(
  ScmInfo(
    url("https://github.com/Sqooba/sq-conf"),
    "scm:git@github.com:Sqooba/sq-conf.git"
  )
)
developers := List(
  Developer("pietrotull", "Pietari Kettunen", "kettunen@gmail.com", url("https://github.com/pietrotull"))
)
