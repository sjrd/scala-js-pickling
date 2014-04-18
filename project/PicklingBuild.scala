import sbt._
import Keys._
import scala.scalajs.sbtplugin.ScalaJSPlugin._

import scala.xml.{Node => XmlNode, NodeSeq => XmlNodeSeq, _}
import scala.xml.transform._

object PicklingBuild extends Build {

  val myScalaJSSettings: Seq[Setting[_]] = scalaJSAbstractSettings ++ Seq(
      resolvers ++= Seq(scalaJSReleasesResolver, scalaJSSnapshotsResolver),
      autoCompilerPlugins := true,
      addCompilerPlugin("org.scala-lang.modules.scalajs" %% "scalajs-compiler" % scalaJSVersion),
      libraryDependencies += "org.scala-lang.modules.scalajs" %% "scalajs-library" % scalaJSVersion
  )

  val needQuasiquotesHack = Def.setting(scalaVersion.value startsWith "2.10.")

  val enableQuasiquotesIn210 = Seq(
      resolvers ++= {
        if (needQuasiquotesHack.value)
          Seq(Resolver.sonatypeRepo("releases"))
        else
          Seq()
      },

      libraryDependencies ++= {
        if (needQuasiquotesHack.value)
          Seq(compilerPlugin("org.scalamacros" % "paradise" % "2.0.0" cross CrossVersion.full),
              "org.scalamacros" %% "quasiquotes" % "2.0.0")
        else
          Seq()
      },

      pomIncludeRepository := {
        if (needQuasiquotesHack.value) { x => false }
        else pomIncludeRepository.value
      },

      pomPostProcess := {
        if (needQuasiquotesHack.value) {
          { (node: XmlNode) =>
            val hardcodeDeps = new RewriteRule {
              override def transform(n: XmlNode): XmlNodeSeq = n match {
                case e: Elem if e != null && e.label == "dependencies" =>
                  // NOTE: this is necessary to unbind from paradise 210
                  // we need to be compiled with paradise 210, because it's the only way to get quasiquotes in 210
                  // however we don't need to be run with paradise 210, because all quasiquotes expand at compile-time
                  // http://docs.scala-lang.org/overviews/macros/paradise.html#macro_paradise_for_210x
                  <dependencies>
                    <dependency>
                        <groupId>org.scala-lang</groupId>
                        <artifactId>scala-library</artifactId>
                        <version>2.10.4</version>
                    </dependency>
                    <dependency>
                        <groupId>org.scala-lang</groupId>
                        <artifactId>scala-reflect</artifactId>
                        <version>2.10.4</version>
                    </dependency>
                    <dependency>
                        <groupId>org.scalamacros</groupId>
                        <artifactId>quasiquotes_2.10</artifactId>
                        <version>2.0.0</version>
                    </dependency>
                  </dependencies>
                case _ => n
              }
            }
            new RuleTransformer(hardcodeDeps).transform(node).head
          }
        } else {
          pomPostProcess.value
        }
      }
  )

}
