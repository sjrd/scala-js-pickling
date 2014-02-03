val commonSettings = Seq(
    organization := "org.scalajs",
    version := "0.1-SNAPSHOT",
    normalizedName ~= { _.replace("scala-js", "scalajs") },
    scalaVersion := "2.10.3",
    crossScalaVersions := Seq("2.10.3", "2.11.0-M7"),
    scalacOptions ++= Seq(
        //"-deprecation", // need to use deprecated things to be compat with 2.10
        "-unchecked",
        "-feature",
        "-encoding", "utf8"
    )
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
      crossScalaVersions := Seq("2.10.3")
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
