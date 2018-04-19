organization := "io.sqooba"
scalaVersion := "2.11.12"
version      := "0.3.0.1-SNAPSHOT"
name         := "sq-conf"

crossScalaVersions := Seq("2.12.5", "2.11.12")

libraryDependencies ++= Seq(
  "com.typesafe"                %   "config"                  % "1.3.2",
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

val artUser = sys.env.get("ARTIFACTORY_USER").getOrElse("")
val artPass = sys.env.get("ARTIFACTORY_PASSWORD").getOrElse("")

credentials += Credentials("Artifactory Realm", "artifactory-v2.sqooba.io", artUser, artPass)

publishTo := {
  val realm = "Artifactory Realm"
  val artBaseUrl = "https://artifactory-v2.sqooba.io/artifactory"
  // isSnapshot := true
  if (isSnapshot.value) {
    Some(realm at s"$artBaseUrl/libs-snapshot-local;build.timestamp=" + new java.util.Date().getTime)
  } else {
    Some(realm at s"$artBaseUrl/libs-release-local")
  }
}
