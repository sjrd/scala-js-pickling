name := "Scala.js pickling core jvm"

libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value
)

enableQuasiquotesIn210
