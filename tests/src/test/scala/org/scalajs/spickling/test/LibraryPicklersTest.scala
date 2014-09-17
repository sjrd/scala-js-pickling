package org.scalajs.spickling
package test

import scala.reflect.ClassTag

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{ literal => lit }

object LibraryPicklersTest extends PicklersTest {

  def litStr(s: String) = lit(t = "java.lang.String", v = s)
  def litInt(i: Int) = lit(t = "java.lang.Integer", v = i)

  describe("Library picklers") {
    it("should be able to pickle a java Date") {
      expectPickleEqual(
        new java.util.Date(0),
        lit(t = "java.util.Date", v = lit(l = 0, m = 0, h = 0)))
    }

    it("should be able to unpickle a java Date") {
      expectUnpickleEqual(
        lit(t = "java.util.Date", v = lit(l = 0, m = 0, h = 0)),
        new java.util.Date(0))
    }

    it("should be able to pickle an empty Map") {
      expectPickleEqual(
        Map(),
        lit(t = "scala.collection.immutable.Map$EmptyMap$", v = js.Array()))
    }

    it("should be able to unpickle an empty Map") {
      expectUnpickleEqual(
        lit(t = "scala.collection.immutable.Map$EmptyMap$", v = js.Array()),
        Map())
    }

    it("should be able to pickle a Map1") {
      expectPickleEqual(
        Map("key1" -> 1),
        lit(t = "scala.collection.immutable.Map$Map1", v = js.Array(lit(k = litStr("key1"), v = litInt(1)))))
    }

    it("should be able to unpickle a Map1") {
      expectUnpickleEqual(
        lit(t = "scala.collection.immutable.Map$Map1", v = js.Array(lit(k = litStr("key1"), v = litInt(1)))),
        Map("key1" -> 1))
    }

    it("should be able to pickle a Map2") {
      expectPickleEqual(
        Map("key1" -> 1, "key2" -> 2),
        lit(t = "scala.collection.immutable.Map$Map2", v = js.Array(lit(k = litStr("key1"), v = litInt(1)), lit(k = litStr("key2"), v = litInt(2)))))
    }

    it("should be able to unpickle a Map1") {
      expectUnpickleEqual(
        lit(t = "scala.collection.immutable.Map$Map2", v = js.Array(lit(k = litStr("key1"), v = litInt(1)), lit(k = litStr("key2"), v = litInt(2)))),
        Map("key1" -> 1, "key2" -> 2))
    }

    it("should be able to pickle a Map3") {
      expectPickleEqual(
        Map("key1" -> 1, "key2" -> 2, "key3" -> 3),
        lit(t = "scala.collection.immutable.Map$Map3", v = js.Array(
          lit(k = litStr("key1"), v = litInt(1)), lit(k = litStr("key2"), v = litInt(2)), lit(k = litStr("key3"), v = litInt(3)))))
    }

    it("should be able to unpickle a Map3") {
      expectUnpickleEqual(
        lit(t = "scala.collection.immutable.Map$Map3", v = js.Array(
          lit(k = litStr("key1"), v = litInt(1)), lit(k = litStr("key2"), v = litInt(2)), lit(k = litStr("key3"), v = litInt(3)))),
        Map("key1" -> 1, "key2" -> 2, "key3" -> 3))
    }

    it("should be able to pickle a Map4") {
      expectPickleEqual(
        Map("key1" -> 1, "key2" -> 2, "key3" -> 3, "key4" -> 4),
        lit(t = "scala.collection.immutable.Map$Map4", v = js.Array(
          lit(k = litStr("key1"), v = litInt(1)), lit(k = litStr("key2"), v = litInt(2)), lit(k = litStr("key3"), v = litInt(3)), lit(k = litStr("key4"), v = litInt(4)))))
    }

    it("should be able to unpickle a Map1") {
      expectUnpickleEqual(
        lit(t = "scala.collection.immutable.Map$Map4", v = js.Array(
          lit(k = litStr("key1"), v = litInt(1)), lit(k = litStr("key2"), v = litInt(2)), lit(k = litStr("key3"), v = litInt(3)), lit(k = litStr("key4"), v = litInt(4)))),
        Map("key1" -> 1, "key2" -> 2, "key3" -> 3, "key4" -> 4))
    }

    it("should be able to pickle a HashTrieMap") {
      expectPickleEqual(
        Map("key1" -> 1, "key2" -> 2, "key3" -> 3, "key4" -> 4, "key5" -> 5),
        lit(t = "scala.collection.immutable.HashMap$HashTrieMap", v = js.Array(
          lit(k = litStr("key4"), v = litInt(4)), lit(k = litStr("key5"), v = litInt(5)),
          lit(k = litStr("key1"), v = litInt(1)), lit(k = litStr("key2"), v = litInt(2)), lit(k = litStr("key3"), v = litInt(3)))))
    }

    it("should be able to unpickle a HashTrieMap") {
      expectUnpickleEqual(
        lit(t = "scala.collection.immutable.HashMap$HashTrieMap", v = js.Array(
          lit(k = litStr("key4"), v = litInt(4)), lit(k = litStr("key5"), v = litInt(5)),
          lit(k = litStr("key1"), v = litInt(1)), lit(k = litStr("key2"), v = litInt(2)), lit(k = litStr("key3"), v = litInt(3)))),
        Map("key1" -> 1, "key2" -> 2, "key3" -> 3, "key4" -> 4, "key5" -> 5))
    }

    it("should be able to pickle an empty Set") {
      expectPickleEqual(
        Set(),
        lit(t = "scala.collection.immutable.Set$EmptySet$", v = js.Array()))
    }

    it("should be able to unpickle an empty Set") {
      expectUnpickleEqual(
        lit(t = "scala.collection.immutable.Set$EmptySet$", v = js.Array()),
        Set())
    }

    it("should be able to pickle a Set1") {
      expectPickleEqual(
        Set("1"),
        lit(t = "scala.collection.immutable.Set$Set1", v = js.Array(litStr("1"))))
    }

    it("should be able to unpickle a Set1") {
      expectUnpickleEqual(
        lit(t = "scala.collection.immutable.Set$Set1", v = js.Array(litStr("1"))),
        Set("1"))
    }

    it("should be able to pickle a Set2") {
      expectPickleEqual(
        Set("1", "2"),
        lit(t = "scala.collection.immutable.Set$Set2", v = js.Array(litStr("1"), litStr("2"))))
    }

    it("should be able to unpickle a Set2") {
      expectUnpickleEqual(
        lit(t = "scala.collection.immutable.Set$Set2", v = js.Array(litStr("1"), litStr("2"))),
        Set("1", "2"))
    }

    it("should be able to pickle a Set3") {
      expectPickleEqual(
        Set("1", "2", "3"),
        lit(t = "scala.collection.immutable.Set$Set3", v = js.Array(litStr("1"), litStr("2"), litStr("3"))))
    }

    it("should be able to unpickle a Set3") {
      expectUnpickleEqual(
        lit(t = "scala.collection.immutable.Set$Set3", v = js.Array(litStr("1"), litStr("2"), litStr("3"))),
        Set("1", "2", "3"))
    }

    it("should be able to pickle a Set4") {
      expectPickleEqual(
        Set("1", "2", "3", "4"),
        lit(t = "scala.collection.immutable.Set$Set4", v = js.Array(litStr("1"), litStr("2"), litStr("3"), litStr("4"))))
    }

    it("should be able to unpickle a Set4") {
      expectUnpickleEqual(
        lit(t = "scala.collection.immutable.Set$Set4", v = js.Array(litStr("1"), litStr("2"), litStr("3"), litStr("4"))),
        Set("1", "2", "3", "4"))
    }

    it("should be able to pickle a HashTrieSet") {
      expectPickleEqual(
        Set("1", "2", "3", "4", "5"),
        lit(t = "scala.collection.immutable.HashSet$HashTrieSet", v = js.Array(litStr("3"), litStr("2"), litStr("1"), litStr("5"), litStr("4"))))
    }

    it("should be able to unpickle a HashTrieSet") {
      expectUnpickleEqual(
        lit(t = "scala.collection.immutable.HashSet$HashTrieSet", v = js.Array(litStr("3"), litStr("2"), litStr("1"), litStr("5"), litStr("4"))),
        Set("1", "2", "3", "4", "5"))
    }
  }
}
