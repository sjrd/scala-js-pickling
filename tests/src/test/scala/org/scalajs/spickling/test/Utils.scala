package org.scalajs.spickling
package test

import scala.reflect.{ ClassTag, classTag }

import scala.scalajs.js

import org.scalajs.spickling.jsany._

import utest._

trait PicklersTest extends TestSuite {
  import PicklersTest._

  def expectPickleEqual(value: Any, expectedPickle: js.Any): Unit = {
    val actualPickleWrapped = new PickleWrapper(PicklerRegistry.pickle(value))
    val expectedPickleWrapped = new PickleWrapper(expectedPickle)
    assert(actualPickleWrapped == expectedPickleWrapped)
  }

  def expectUnpickleEqual(pickle: js.Any, expectedValue: Any): Unit = {
    val actualValue = PicklerRegistry.unpickle(pickle)
    assert(actualValue == expectedValue)
  }
}

object PicklersTest {
  private def pickleEquals(actual: Any, expected: Any): Boolean = {
    actual == expected || {
      js.typeOf(actual.asInstanceOf[js.Any]) == "object" &&
      js.typeOf(expected.asInstanceOf[js.Any]) == "object" && {
        val actualDict = actual.asInstanceOf[js.Dictionary[Any]]
        val expectedDict = expected.asInstanceOf[js.Dictionary[Any]]
        actualDict.keySet == expectedDict.keySet && actualDict.forall {
          case (key, value) => pickleEquals(value, expectedDict(key))
        }
      }
    }
  }

  class PickleWrapper(val self: js.Any) {
    override def toString(): String =
      js.JSON.stringify(self)

    override def equals(that: Any): Boolean = that match {
      case that: PickleWrapper => pickleEquals(self, that.self)
      case _                   => false
    }
  }
}
