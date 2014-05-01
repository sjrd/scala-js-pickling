scalaJSSettings

name := "Scala.js pickling core"

libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value
)

enableQuasiquotesIn210
