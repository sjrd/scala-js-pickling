package org.scalajs.spickling.playjson

import org.scalajs.spickling.{PBuilder, PReader}
import play.api.libs.json._

object PlayJsonPBuilder extends PBuilder[JsValue] {
  def makeNull(): JsValue = JsNull
  def makeBoolean(b: Boolean): JsValue = JsBoolean(b)
  def makeNumber(x: Double): JsValue = JsNumber(x)
  def makeString(s: String): JsValue = JsString(s)
  def makeArray(elems: JsValue*): JsValue = JsArray(elems)
  def makeObject(fields: (String, JsValue)*): JsValue = JsObject(fields)
}

object PlayJsonPReader extends PReader[JsValue] {
  def isUndefined(x: JsValue): Boolean = x.isInstanceOf[JsUndefined]
  def isNull(x: JsValue): Boolean = x == JsNull
  def readBoolean(x: JsValue): Boolean = x.as[Boolean]
  def readNumber(x: JsValue): Double = x.as[Double]
  def readString(x: JsValue): String = x.as[String]
  def readArrayLength(x: JsValue): Int = x.asInstanceOf[JsArray].value.size
  def readArrayElem(x: JsValue, index: Int): JsValue =
    x.asInstanceOf[JsArray].apply(index)
  def readObjectField(x: JsValue, field: String): JsValue =
    x.asInstanceOf[JsObject] \ field
}
