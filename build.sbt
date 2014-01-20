val commonSettings = Seq(
    organization := "org.scalajs",
    version := "0.1-SNAPSHOT",
    normalizedName ~= { _.replace("scala-js", "scalajs") },
    scalaVersion := "2.11.0-M7",
    crossScalaVersions := Seq("2.11.0-M7", "2.10.3"),
    scalacOptions ++= Seq(
        //"-deprecation", // need to use deprecated things to be compat with 2.10
        "-unchecked",
        "-feature",
        "-encoding", "utf8"
    )
)

lazy val root = project.in(file("."))
  .settings(commonSettings: _*)
  .aggregate(core)

lazy val core = project
  .settings(commonSettings: _*)

// tests must be in a separate project for the IDE not to choke on macros
lazy val tests = project
  .settings(commonSettings: _*)
  .dependsOn(core)
