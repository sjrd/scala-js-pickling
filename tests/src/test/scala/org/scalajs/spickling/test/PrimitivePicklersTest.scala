package org.scalajs.spickling
package test

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{ literal => lit }

import utest._

object PrimitivePicklersTest extends PicklersTest {

  val tests = TestSuite {
    "pickle a Boolean" - {
      expectPickleEqual(
          true,
          lit(t = "java.lang.Boolean", v = true))
    }

    "unpickle a Boolean" - {
      expectUnpickleEqual(
          lit(t = "java.lang.Boolean", v = true),
          true)
    }

    "pickle an Int" - {
      expectPickleEqual(
          42,
          lit(t = "java.lang.Integer", v = 42))
    }

    "unpickle an Int" - {
      expectUnpickleEqual(
          lit(t = "java.lang.Integer", v = 42),
          42)
    }

    "pickle a Long" - {
      expectPickleEqual(
          42L,
          lit(t = "java.lang.Long", v = lit(l = 42, m = 0, h = 0)))
    }

    "unpickle a Long" - {
      expectUnpickleEqual(
          lit(t = "java.lang.Long", v = lit(l = 42, m = 0, h = 0)),
          42L)
    }

    "pickle a String" - {
      expectPickleEqual(
          "hello",
          lit(t = "java.lang.String", v = "hello"))
    }

    "unpickle a String" - {
      expectUnpickleEqual(
          lit(t = "java.lang.String", v = "hello"),
          "hello")
    }

    "pickle null" - {
      expectPickleEqual(null, null)
    }

    "unpickle null" - {
      expectUnpickleEqual(null, null)
    }
  }
}
