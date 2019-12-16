organization  := "io.sqooba"
scalaVersion  := "2.13.1"
name          := "sq-conf"
description   := "Unified configuration interface."
homepage      := Some(url("https://github.com/Sqooba/sq-conf"))
licenses      := Seq("Apache 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0"))

crossScalaVersions := Seq("2.13.1", "2.12.10", "2.11.12")

// scalatest available in 2.13, scala-logging not yet (only 2.13-RC3)
libraryDependencies ++= Seq(
  "com.typesafe"                %   "config"                  % "1.4.0",
  "com.typesafe.scala-logging"  %%  "scala-logging"           % "3.9.2",
  "ch.qos.logback"              %   "logback-classic"         % "1.2.3"             % Test,
  "org.scalatest"               %%  "scalatest"               % "3.0.8"             % Test,
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


