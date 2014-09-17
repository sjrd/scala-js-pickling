package org.scalajs.spickling

import java.util.{Date => JDate}
import scala.collection.immutable.Map.{Map1, Map2, Map3, Map4}
import scala.collection.immutable.HashMap.HashTrieMap
import scala.collection.immutable.Set.{Set1, Set2, Set3, Set4}
import scala.collection.immutable.HashSet.HashTrieSet

trait Unpickler[A] {
  type Unpicklee = A

  def unpickle[P](pickle: P)(implicit registry: PicklerRegistry,
      reader: PReader[P]): A
}

object Unpickler extends PicklerMaterializers {
  implicit object BooleanUnpickler extends Unpickler[Boolean] {
    def unpickle[P](pickle: P)(implicit registry: PicklerRegistry,
        reader: PReader[P]): Boolean = reader.readBoolean(pickle)
  }

  implicit object CharUnpickler extends Unpickler[Char] {
    def unpickle[P](pickle: P)(implicit registry: PicklerRegistry,
        reader: PReader[P]): Char = reader.readString(pickle).charAt(0)
  }

  implicit object ByteUnpickler extends Unpickler[Byte] {
    def unpickle[P](pickle: P)(implicit registry: PicklerRegistry,
        reader: PReader[P]): Byte = reader.readNumber(pickle).toByte
  }

  implicit object ShortUnpickler extends Unpickler[Short] {
    def unpickle[P](pickle: P)(implicit registry: PicklerRegistry,
        reader: PReader[P]): Short = reader.readNumber(pickle).toShort
  }

  implicit object IntUnpickler extends Unpickler[Int] {
    def unpickle[P](pickle: P)(implicit registry: PicklerRegistry,
        reader: PReader[P]): Int = reader.readNumber(pickle).toInt
  }

  implicit object LongUnpickler extends Unpickler[Long] {
    def unpickle[P](pickle: P)(implicit registry: PicklerRegistry,
        reader: PReader[P]): Long = {
      // FIXME This is probably wrong wrt negative numbers
      val l = reader.readNumber(reader.readObjectField(pickle, "l"))
      val m = reader.readNumber(reader.readObjectField(pickle, "m"))
      val h = reader.readNumber(reader.readObjectField(pickle, "h"))
      (h.toLong << 44) | (m.toLong << 22) | l.toLong
    }
  }

  implicit object FloatUnpickler extends Unpickler[Float] {
    def unpickle[P](pickle: P)(implicit registry: PicklerRegistry,
        reader: PReader[P]): Float = reader.readNumber(pickle).toFloat
  }

  implicit object DoubleUnpickler extends Unpickler[Double] {
    def unpickle[P](pickle: P)(implicit registry: PicklerRegistry,
        reader: PReader[P]): Double = reader.readNumber(pickle).toDouble
  }

  implicit object StringUnpickler extends Unpickler[String] {
    def unpickle[P](pickle: P)(implicit registry: PicklerRegistry,
        reader: PReader[P]): String = reader.readString(pickle)
  }

  /*
   * JDate
   */

  implicit object JDateUnpickler extends Unpickler[JDate] {
    def unpickle[P](pickle: P)(implicit registry: PicklerRegistry, reader: PReader[P]): JDate =
      new JDate(Unpickler.LongUnpickler.unpickle(pickle)(registry, reader))
  }

  /*
   * Map
   */

  trait BaseMapUnpickler[A <: Map[Any,Any]] extends Unpickler[A] {
    def unpickle[P](pickle: P)(implicit registry: PicklerRegistry, reader: PReader[P]): A = {
      val len = reader.readArrayLength(pickle)
      Map((0 to len-1).map { i =>
        val obj = reader.readArrayElem(pickle, i)
        val key = registry.unpickle(reader.readObjectField(obj, "k"))
        val value = registry.unpickle(reader.readObjectField(obj, "v"))
        (key, value)
      }: _*).asInstanceOf[A] // FIXME: to avoid
    }
  }

  implicit object MapUnpickler extends BaseMapUnpickler[Map[Any,Any]]
  implicit object Map1Unpickler extends BaseMapUnpickler[Map1[Any,Any]]
  implicit object Map2Unpickler extends BaseMapUnpickler[Map2[Any,Any]]
  implicit object Map3Unpickler extends BaseMapUnpickler[Map3[Any,Any]]
  implicit object Map4Unpickler extends BaseMapUnpickler[Map4[Any,Any]]
  implicit object HashTrieMapUnpickler extends BaseMapUnpickler[HashTrieMap[Any,Any]]

  /*
   * Set
   */

  trait BaseSetUnpickler[A <: Set[Any]] extends Unpickler[A] {
    def unpickle[P](pickle: P)(implicit registry: PicklerRegistry, reader: PReader[P]): A = {
      val len = reader.readArrayLength(pickle)
      Set((0 to len-1).map { i =>
        registry.unpickle(reader.readArrayElem(pickle, i))
      }: _*).asInstanceOf[A] // FIXME: to avoid
    }
  }

  implicit object SetUnpickler extends BaseSetUnpickler[Set[Any]]
  implicit object Set1Unpickler extends BaseSetUnpickler[Set1[Any]]
  implicit object Set2Unpickler extends BaseSetUnpickler[Set2[Any]]
  implicit object Set3Unpickler extends BaseSetUnpickler[Set3[Any]]
  implicit object Set4Unpickler extends BaseSetUnpickler[Set4[Any]]
  implicit object HashTrieSetUnpickler extends BaseSetUnpickler[HashTrieSet[Any]]
}
