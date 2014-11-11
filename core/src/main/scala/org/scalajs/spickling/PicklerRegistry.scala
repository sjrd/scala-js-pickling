package org.scalajs.spickling

import scala.reflect.ClassTag
import scala.collection.mutable

object PicklerRegistry extends CollectionRegistry {
  class SingletonFullName[A](val name: String)

  object SingletonFullName extends PicklerMaterializers
}

trait PicklerRegistry {
  def pickle[P](value: Any)(implicit builder: PBuilder[P],
      registry: PicklerRegistry = this): P
  def unpickle[P](pickle: P)(implicit reader: PReader[P],
      registry: PicklerRegistry = this): Any
}


