val commonSettings = Seq(
    organization := "org.scalajs",
    version := "0.3-SNAPSHOT",
    normalizedName ~= { _.replace("scala-js", "scalajs") },
    homepage := Some(url("http://scala-js.org/")),
    licenses += ("BSD New", url("https://github.com/scala-js/scala-js/blob/master/LICENSE")),
    scalaVersion := "2.10.4",
    crossScalaVersions := Seq("2.10.4", "2.11.0"),
    scalacOptions ++= Seq(
        //"-deprecation", // need to use deprecated things to be compat with 2.10
        "-unchecked",
        "-feature",
        "-encoding", "utf8"
    ),
    //
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
  .settings(commonSettings: _*)

lazy val corejvm = project
  .settings(commonSettings: _*)
  .settings(
      sourceDirectory := (sourceDirectory in core).value
  )

lazy val js = project
  .settings(commonSettings: _*)
  .dependsOn(core)

lazy val playjson = project
  .settings(commonSettings: _*)
  .settings(
      // Play is not built against 2.11
      crossScalaVersions := Seq("2.10.4")
  )
  .dependsOn(corejvm)

// tests must be in a separate project for the IDE not to choke on macros
lazy val tests = project
  .settings(commonSettings: _*)
  .settings(
      publish := {},
      publishLocal := {}
  )
  .dependsOn(js)
