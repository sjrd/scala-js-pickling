import PicklingBuild.enableQuasiquotesIn210

val commonSettings = Seq(
    organization := "be.doeraene",
    version := "0.4.1-SNAPSHOT",
    normalizedName ~= { _.replace("scala-js", "scalajs") },
    homepage := Some(url("http://scala-js.org/")),
    licenses += ("BSD 3-Clause", url("http://opensource.org/licenses/BSD-3-Clause")),
    scalaVersion := "2.11.5",
    crossScalaVersions := Seq("2.10.4", "2.11.5"),
    scalacOptions ++= Seq(
        //"-deprecation", // need to use deprecated things to be compat with 2.10
        "-unchecked",
        "-feature",
        "-encoding", "utf8"
    ),

    scmInfo := Some(ScmInfo(
        url("https://github.com/scala-js/scala-js-pickling"),
        "scm:git:git@github.com:scala-js/scala-js-pickling.git",
        Some("scm:git:git@github.com:scala-js/scala-js-pickling.git"))),

    publishMavenStyle := true,

    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },

    pomExtra := (
      <developers>
        <developer>
          <id>sjrd</id>
          <name>Sébastien Doeraene</name>
          <url>https://github.com/sjrd/</url>
        </developer>
      </developers>
    ),

    pomIncludeRepository := { _ => false }
)

lazy val root = project.in(file("."))
  .settings(commonSettings: _*)
  .settings(
      publish := {},
      publishLocal := {}
  )
  .aggregate(corejs, corejvm, js, playjson, tests)

lazy val core = crossProject.crossType(CrossType.Pure)
  .settings(commonSettings: _*)
  .settings(enableQuasiquotesIn210: _*)
  .settings(
    name := "Scala.js pickling core",
    libraryDependencies +=
      "org.scala-lang" % "scala-reflect" % scalaVersion.value
  )

lazy val corejvm = core.jvm
lazy val corejs = core.js

lazy val js = project
  .enablePlugins(ScalaJSPlugin)
  .settings(commonSettings: _*)
  .settings(
    name := "Scala.js pickling"
  )
  .dependsOn(corejs)

lazy val playjson = project
  .settings(commonSettings: _*)
  .settings(
    name := "Scala.js pickling play-json",
    resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/maven-releases/",
    libraryDependencies += "com.typesafe.play" %% "play-json" % "2.3.0"
  )
  .dependsOn(corejvm)

// tests must be in a separate project for the IDE not to choke on macros
lazy val tests = project
  .enablePlugins(ScalaJSPlugin)
  .settings(commonSettings: _*)
  .settings(
    publish := {},
    publishLocal := {},
    name := "Scala.js pickling tests",
    libraryDependencies +=
      "com.lihaoyi" %%% "utest" % "0.3.0" % "test",
    testFrameworks += new TestFramework("utest.runner.Framework")
  )
  .dependsOn(js)
