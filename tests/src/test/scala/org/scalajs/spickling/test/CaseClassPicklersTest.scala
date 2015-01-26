package org.scalajs.spickling
package test

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{ literal => lit }

import utest._

case class Person(name: String, age: Int)

case object TrivialCaseObject

object CaseClassPicklersTest extends PicklersTest {

  PicklerRegistry.register[Person]
  PicklerRegistry.register(TrivialCaseObject)

  val tests = TestSuite {
    "pickle a Person" - {
      expectPickleEqual(
          Person("Jack", 24),
          lit(t = "org.scalajs.spickling.test.Person", v = lit(
              name = lit(t = "java.lang.String", v = "Jack"),
              age = lit(t = "java.lang.Integer", v = 24))))
    }

    "unpickle a Person" - {
      expectUnpickleEqual(
          lit(t = "org.scalajs.spickling.test.Person", v = lit(
              name = lit(t = "java.lang.String", v = "Jack"),
              age = lit(t = "java.lang.Integer", v = 24))),
          Person("Jack", 24))
    }

    "pickle TrivialCaseObject" - {
      expectPickleEqual(
          TrivialCaseObject,
          lit(s = "org.scalajs.spickling.test.TrivialCaseObject$"))
    }

    "unpickle TrivialCaseObject" - {
      expectUnpickleEqual(
          lit(s = "org.scalajs.spickling.test.TrivialCaseObject$"),
          TrivialCaseObject)
    }
  }
}
