package be.doeraene.spickling

import scala.language.experimental.macros

import scala.reflect.macros.Context

object PicklerMaterializersImpl {
  def materializePickler[T: c.WeakTypeTag](c: Context): c.Expr[Pickler[T]] = {
    import c.universe._

    val tpe = weakTypeOf[T]
    val sym = tpe.typeSymbol.asClass

    if (!sym.isCaseClass) {
      c.error(c.enclosingPosition,
          "Cannot materialize pickler for non-case class")
      return c.Expr[Pickler[T]](q"null")
    }

    val accessors = (tpe.declarations collect {
      case acc: MethodSymbol if acc.isCaseAccessor => acc
    }).toList

    val pickleFields = for {
      accessor <- accessors
    } yield {
      val fieldName = accessor.name.toTermName
      val fieldString = fieldName.toString()
      q"""
        ($fieldString, registry.pickle(value.$fieldName))
      """
    }

    val pickleLogic = q"""
      builder.makeObject(..$pickleFields)
    """

    val result = q"""
      implicit object GenPickler extends be.doeraene.spickling.Pickler[$tpe] {
        import be.doeraene.spickling._
        override def pickle[P](value: $tpe)(
            implicit registry: PicklerRegistry,
            builder: PBuilder[P]): P = $pickleLogic
      }
      GenPickler
    """

    c.Expr[Pickler[T]](result)
  }

  def materializeUnpickler[T: c.WeakTypeTag](c: Context): c.Expr[Unpickler[T]] = {
    import c.universe._

    val tpe = weakTypeOf[T]
    val sym = tpe.typeSymbol.asClass

    if (!sym.isCaseClass) {
      c.error(c.enclosingPosition,
          "Cannot materialize pickler for non-case class")
      return c.Expr[Unpickler[T]](q"null")
    }

    val accessors = (tpe.declarations collect {
      case acc: MethodSymbol if acc.isCaseAccessor => acc
    }).toList

    val unpickledFields = for {
      accessor <- accessors
    } yield {
      val fieldName = accessor.name
      val fieldString = fieldName.toString()
      val fieldTpe = accessor.returnType
      q"""
        registry.unpickle(reader.readObjectField(
            pickle, $fieldString)).asInstanceOf[$fieldTpe]
      """
    }

    val unpickleLogic = q"""
      new $tpe(..$unpickledFields)
    """

    val result = q"""
      implicit object GenUnpickler extends be.doeraene.spickling.Unpickler[$tpe] {
        import be.doeraene.spickling._
        override def unpickle[P](pickle: P)(
            implicit registry: PicklerRegistry,
      reader: PReader[P]): $tpe = $unpickleLogic
      }
      GenUnpickler
    """

    c.Expr[Unpickler[T]](result)
  }

  def materializeCaseObjectName[T: c.WeakTypeTag](
      c: Context): c.Expr[PicklerRegistry.SingletonFullName[T]] = {
    import c.universe._

    val tpe = weakTypeOf[T]
    val sym = tpe.typeSymbol.asClass

    if (!sym.isModuleClass || !sym.isCaseClass)
      c.abort(c.enclosingPosition,
          s"Cannot generate a case object name for non-case object $sym")

    val name = sym.fullName+"$"
    val result = q"""
      new be.doeraene.spickling.PicklerRegistry.SingletonFullName[$tpe]($name)
    """

    c.Expr[PicklerRegistry.SingletonFullName[T]](result)
  }
}

trait PicklerMaterializers {
  implicit def materializePickler[T]: Pickler[T] =
    macro PicklerMaterializersImpl.materializePickler[T]

  implicit def materializeUnpickler[T]: Unpickler[T] =
    macro PicklerMaterializersImpl.materializeUnpickler[T]

  implicit def materializeCaseObjectName[T]: PicklerRegistry.SingletonFullName[T] =
    macro PicklerMaterializersImpl.materializeCaseObjectName[T]
}
