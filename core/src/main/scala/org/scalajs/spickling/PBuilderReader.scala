package org.scalajs.spickling

trait PBuilder[P] {
  def makeNull(): P
  def makeBoolean(b: Boolean): P
  def makeNumber(x: Double): P
  def makeString(s: String): P
  def makeArray(elems: P*): P
  def makeObject(fields: (String, P)*): P
}

trait PReader[P] {
  def isUndefined(x: P): Boolean
  def isNull(x: P): Boolean
  def readBoolean(x: P): Boolean
  def readNumber(x: P): Double
  def readString(x: P): String
  def readArrayLength(x: P): Int
  def readArrayElem(x: P, index: Int): P
  def readObjectField(x: P, field: String): P
}
