organization := "io.sqooba"
scalaVersion := "2.12.4"
version      := "0.1.1"
name         := "sq-conf"

crossScalaVersions := Seq("2.12.4", "2.11.11")

libraryDependencies ++= Seq(
  "com.typesafe"                %   "config"                  % "1.3.1",
  "com.typesafe.scala-logging"  %%  "scala-logging"           % "3.7.2",
  "ch.qos.logback"              %   "logback-classic"         % "1.2.3"             % Test,
  "org.scalatest"               %%  "scalatest"               % "3.0.3"             % Test,
  "org.mockito"                 %   "mockito-all"             % "1.10.19"           % Test
)

excludeDependencies ++= Seq("org.slf4j" % "slf4j-log4j12", "log4j" % "log4j")

testOptions in Test += Tests.Argument("-l", "ExternalSpec")

lazy val External = config("ext").extend(Test)
configs(External)
inConfig(External)(Defaults.testTasks)
testOptions in External -= Tests.Argument("-l", "ExternalSpec")
testOptions in External += Tests.Argument("-n", "ExternalSpec")

publishTo := {
  val realm = "Artifactory Realm"
  val artBaseUrl = "https://artifactory.sqooba.io/artifactory"
  if (isSnapshot.value) {
    Some(realm at s"$artBaseUrl/libs-snapshot-local;build.timestamp=" + new java.util.Date().getTime)
  } else {
    Some(realm at s"$artBaseUrl/libs-release-local")
  }
}
