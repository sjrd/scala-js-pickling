package org.scalajs.spickling

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
}
