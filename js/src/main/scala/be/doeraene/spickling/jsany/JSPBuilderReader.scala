package be.doeraene.spickling.jsany

import be.doeraene.spickling.{PBuilder, PReader}
import scala.scalajs.js

object JSPBuilder extends PBuilder[js.Any] {
  def makeNull(): js.Any = null
  def makeBoolean(b: Boolean): js.Any = b
  def makeNumber(x: Double): js.Any = x
  def makeString(s: String): js.Any = s
  def makeArray(elems: js.Any*): js.Any = js.Array(elems: _*)
  def makeObject(fields: (String, js.Any)*): js.Any = {
    val result = js.Dictionary.empty[js.Any]
    for ((prop, value) <- fields)
      result(prop) = value
    result
  }
}

object JSPReader extends PReader[js.Any] {
  def isUndefined(x: js.Any): Boolean = x.isInstanceOf[Unit]
  def isNull(x: js.Any): Boolean = x eq null
  def readBoolean(x: js.Any): Boolean = x.asInstanceOf[Boolean]
  def readNumber(x: js.Any): Double = x.asInstanceOf[Double]
  def readString(x: js.Any): String = x.asInstanceOf[String]
  def readArrayLength(x: js.Any): Int = x.asInstanceOf[js.Array[_]].length.toInt
  def readArrayElem(x: js.Any, index: Int): js.Any =
    x.asInstanceOf[js.Array[js.Any]].apply(index)
  def readObjectField(x: js.Any, field: String): js.Any =
    x.asInstanceOf[js.Dynamic].selectDynamic(field)
}
