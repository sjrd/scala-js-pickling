package org.scalajs.spickling.jsany

import org.scalajs.spickling.{PBuilder, PReader}
import scala.scalajs.js
import scala.scalajs.js.prim.Undefined
import scala.scalajs.js.prim.Number

/**
 * Builder that is used to create JSON
 */
object JSPBuilder extends PBuilder[js.Any]
{
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

/**
 * Reader that reads JSON
 */
object JSPReader extends PReader[js.Any] {
  def isUndefined(x: js.Any): Boolean = x.isInstanceOf[Undefined]
  def isNull(x: js.Any): Boolean = x eq null
  def readBoolean(x: js.Any): Boolean = x.asInstanceOf[Boolean]
  def readNumber(x: js.Any): Double = x.asInstanceOf[Number]
  def readString(x: js.Any): String = x.asInstanceOf[String]
  def readArrayLength(x: js.Any): Int = x.asInstanceOf[js.Array[_]].length
  def readArrayElem(x: js.Any, index: Int): js.Any =
    x.asInstanceOf[js.Array[js.Any]].apply(index)
  def readObjectField(x: js.Any, field: String): js.Any =
    x.asInstanceOf[js.Dictionary[js.Any]].apply(field)
}
