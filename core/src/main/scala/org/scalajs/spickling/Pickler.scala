package org.scalajs.spickling

trait Pickler[A] {
  type Picklee = A

  def pickle[P](obj: Picklee)(implicit registry: PicklerRegistry,
      builder: PBuilder[P]): P
}

object Pickler extends PicklerMaterializers {
  implicit object BooleanPickler extends Pickler[Boolean] {
    def pickle[P](x: Boolean)(implicit registry: PicklerRegistry,
        builder: PBuilder[P]): P = builder.makeBoolean(x)
  }

  implicit object CharPickler extends Pickler[Char] {
    def pickle[P](x: Char)(implicit registry: PicklerRegistry,
        builder: PBuilder[P]): P = builder.makeString(x.toString)
  }

  implicit object BytePickler extends Pickler[Byte] {
    def pickle[P](x: Byte)(implicit registry: PicklerRegistry,
        builder: PBuilder[P]): P = builder.makeNumber(x)
  }

  implicit object ShortPickler extends Pickler[Short] {
    def pickle[P](x: Short)(implicit registry: PicklerRegistry,
        builder: PBuilder[P]): P = builder.makeNumber(x)
  }

  implicit object IntPickler extends Pickler[Int] {
    def pickle[P](x: Int)(implicit registry: PicklerRegistry,
        builder: PBuilder[P]): P = builder.makeNumber(x)
  }

  implicit object LongPickler extends Pickler[Long] {
    def pickle[P](x: Long)(implicit registry: PicklerRegistry,
        builder: PBuilder[P]): P = {
      builder.makeObject(
          ("l", builder.makeNumber(x.toInt & 0x3fffff)),
          ("m", builder.makeNumber((x >> 22).toInt & 0x3fffff)),
          ("h", builder.makeNumber((x >> 44).toInt)))
    }
  }

  implicit object FloatPickler extends Pickler[Float] {
    def pickle[P](x: Float)(implicit registry: PicklerRegistry,
        builder: PBuilder[P]): P = builder.makeNumber(x)
  }

  implicit object DoublePickler extends Pickler[Double] {
    def pickle[P](x: Double)(implicit registry: PicklerRegistry,
        builder: PBuilder[P]): P = builder.makeNumber(x)
  }

  implicit object StringPickler extends Pickler[String] {
    def pickle[P](x: String)(implicit registry: PicklerRegistry,
        builder: PBuilder[P]): P = builder.makeString(x)
  }
}
