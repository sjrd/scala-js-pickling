import PicklingBuild.enableQuasiquotesIn210

val commonSettings = Seq(
    organization := "org.scalajs",
    version := "0.4-SNAPSHOT",
    normalizedName ~= { _.replace("scala-js", "scalajs") },
    homepage := Some(url("http://scala-js.org/")),
    licenses += ("BSD New", url("https://github.com/scala-js/scala-js/blob/master/LICENSE")),
    scalaVersion := "2.11.5",
    crossScalaVersions := Seq("2.10.4", "2.11.5"),
    scalacOptions ++= Seq(
        //"-deprecation", // need to use deprecated things to be compat with 2.10
        "-unchecked",
        "-feature",
        "-encoding", "utf8"
    ),

    publishTo := {
      val isSnapshot = version.value.endsWith("-SNAPSHOT")
      val snapshotsOrReleases = if (isSnapshot) "snapshots" else "releases"
      val resolver = Resolver.sftp(
          s"scala-js-$snapshotsOrReleases-publish",
          "repo.scala-js.org",
          s"/home/scalajsrepo/www/repo/$snapshotsOrReleases")(Resolver.ivyStylePatterns)
      Seq("PUBLISH_USER", "PUBLISH_PASS").map(scala.util.Properties.envOrNone) match {
        case Seq(Some(user), Some(pass)) =>
          Some(resolver as (user, pass))
        case _ =>
          None
      }
    },
    publishMavenStyle := false
)

lazy val root = project.in(file("."))
  .settings(commonSettings: _*)
  .settings(
      publish := {},
      publishLocal := {}
  )
  .aggregate(core, corejvm, js, playjson, tests)

lazy val core = project
  .enablePlugins(ScalaJSPlugin)
  .settings(commonSettings: _*)
  .settings(enableQuasiquotesIn210: _*)
  .settings(
    name := "Scala.js pickling core",
    libraryDependencies +=
      "org.scala-lang" % "scala-reflect" % scalaVersion.value
  )

lazy val corejvm = project
  .settings(commonSettings: _*)
  .settings(enableQuasiquotesIn210: _*)
  .settings(
    sourceDirectory := (sourceDirectory in core).value,
    name := "Scala.js pickling core jvm",
    libraryDependencies +=
      "org.scala-lang" % "scala-reflect" % scalaVersion.value
  )

lazy val js = project
  .enablePlugins(ScalaJSPlugin)
  .settings(commonSettings: _*)
  .settings(
    name := "Scala.js pickling"
  )
  .dependsOn(core)

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
