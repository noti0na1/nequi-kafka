val mainScala = "2.12.8"
val allScala  = Seq("2.11.12", mainScala)

inThisBuild(
  List(
    organization := "com.nequissimus",
    homepage := Some(url("http://nequissimus.com/")),
    licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    scalaVersion := mainScala,
    developers := List(
      Developer(
        "NeQuissimus",
        "Tim Steinbach",
        "steinbach.tim@gmail.com",
        url("http://nequissimus.com/")
      )
    ),
    parallelExecution in Test := false,
    fork in Test := true
  )
)

val commonSettings = Seq(
  libraryDependencies ++= Seq(
    "org.apache.kafka"        %% "kafka-streams-scala"    % "2.1.0",
    "com.lihaoyi"             %% "utest"                  % "0.6.6" % Test,
    "io.github.embeddedkafka" %% "embedded-kafka-streams" % "2.1.0" % Test,
    "javax.ws.rs"             % "javax.ws.rs-api"         % "2.1.1" artifacts (Artifact("javax.ws.rs-api", "jar", "jar")) // https://github.com/sbt/sbt/issues/3618
  ),
  testFrameworks += new TestFramework("utest.runner.Framework"),
  scalacOptions in Test ++= Seq(
    "-deprecation",
    "-encoding",
    "UTF-8",
    "-explaintypes",
    "-Yrangepos",
    "-feature",
    "-Xfuture",
    "-Ypartial-unification",
    "-language:higherKinds",
    "-language:existentials",
    "-unchecked",
    "-Yno-adapted-args",
    "-Xlint:_,-type-parameter-shadow",
    "-Ywarn-inaccessible",
    "-Ywarn-infer-any",
    "-Ywarn-nullary-override",
    "-Ywarn-nullary-unit",
    "-Ywarn-numeric-widen",
    "-Ywarn-unused",
    "-Ywarn-value-discard"
  ) ++ (CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, scalaMajor)) if scalaMajor <= 11 => Nil
    case _ =>
      Seq(
        "-opt-warnings",
        "-Ywarn-extra-implicit",
        "-opt:l:inline",
        "-opt-inline-from:<source>",
        "-Xsource:2.13"
      )
  }),
  crossScalaVersions := allScala
)

lazy val root = project
  .in(file("."))
  .settings(
    skip in publish := true
  )
  .aggregate(statsd)

lazy val statsd = project
  .in(file("./statsd"))
  .settings(commonSettings)
  .settings(
    name := "kafka-streams-statsd",
    libraryDependencies ++= Seq(
      "com.github.gphat" %% "censorinus" % "2.1.15"
    ),
  )
