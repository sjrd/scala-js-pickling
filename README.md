# Scala.js Pickling 0.1

Scala.js Pickling is a small serialization (aka pickling) library for
[Scala.js](https://www.scala-js.org/). It also cross-compiles on the JVM so
that client and server can exchange pickled data transparently. It was inspired
by [Scala Pickling](https://github.com/scala/pickling), but a major requirement
is that everything happens at compile-time, and that pickling and unpickling
never falls back on runtime reflection.

Therefore, types that have to be pickled and unpickled must be registered
explicitly, (at least) once, in a `PicklerRegistry` beforehand:

```scala
import org.scalajs.spickling._

// custom data types
case class Person(name: String, age: Int)
case object TrivialCaseObject

// register the data types once
PicklerRegistry.register[Person]
PicklerRegistry.register(TrivialCaseObject)
```

`register` expects implicit picklers and unpicklers for the type. This works
out of the box for case classes and case objects. See the reference below for
more details and how to create custom picklers.

After that, the case class `Person` and the case object `TrivialCaseObject`
can be pickled an unpickled using the following in Scala.js:

```scala
// import the implicits for the js.Any pickle format (~JSON objects)
import org.scalajs.spickling.jsany._

// pickle and unpickle
val pickle: js.Any = PicklerRegistry.pickle(Person("John", 24))
val value: Any = PicklerRegistry.unpickle(pickle)
```

The `js.Any` pickle format consists of typical JSON-serializable JavaScript
values (primitives, arrays and dictionaries).

On the JVM, a pickle format for the Play! JSON library is provided by default,
and is used similarly:

```scala
// import the implicits for the Play! JSON pickle format
import org.scalajs.spickling.playjson._

// pickle and unpickle
val pickle: JsValue = PicklerRegistry.pickle(Person("John", 24))
val value: Any = PicklerRegistry.unpickle(pickle)
```

## Getting Started

Scala.js Pickling is published in the
[scala-js-releases repo on Bintray](https://bintray.com/scala-js/scala-js-releases),
which is resolved by default for Scala.js projects.

Hence, in a Scala.js project, all you need to do is to add the following to
your `build.sbt`:

```scala
libraryDependencies += "org.scalajs" %% "scalajs-pickling" % "0.1"
```

On the server side of a Play! application, you will need to declare the
resolver explicitly, and use the `scalajs-pickling-play-json` package:

```scala
resolvers += Resolver.url("scala-js-releases",
    url("http://dl.bintray.com/content/scala-js/scala-js-releases"))(
    Resolver.ivyStylePatterns)

libraryDependencies += "org.scalajs" %% "scalajs-pickling-play-json" % "0.1"
```

## Reference

The basic snippets hereabove should get you started quickly. But it is probably
worth reading this section to understand how the public API is articulated.

### PicklerRegistry

The main entry point to the library is the `PicklerRegistry`. At its core, a
pickler registry is a thing that can pickle and unpickle values:

```scala
trait PicklerRegistry {
  def pickle[P](value: Any)(implicit builder: PBuilder[P],
      registry: PicklerRegistry = this): P
  def unpickle[P](pickle: P)(implicit reader: PReader[P],
      registry: PicklerRegistry = this): Any
}
```

Throughout the codebase and the API, `P` is the type of a pickle in a given
format. `P` can be anything, really. The way the API interacts with generic
`P` is through implicit pickle builders (`PBuilder[P]`) and pickle readers
(`PReader[P]`). More on this later.

The basic implementation of `PicklerRegistry` is `BasePicklerRegistry`, and,
for convenience, the top-level object `PicklerRegistry` is an instance of
`BasePicklerRegistry`.

A `BasePicklerRegistry`, by default, can only pickle primitive types and
strings. Other data types must be registered explicitly using one of its
`register` methods. There are two main `register` methods: one for types,
and one for values (typically singleton objects):

```scala
class BasePicklerRegistry extends PicklerRegistry {
  def register[A : ClassTag](implicit pickler: Pickler[A],
      unpickler: Unpickler[A]): Unit = { ... }

  def register[A <: Singleton](obj: A)(
      implicit name: SingletonFullName[A]): Unit = { ... }

  ...
}

object PicklerRegistry extends BasePicklerRegistry
```

Throughout the codebase and the API, `A` is the type of a value being pickled
or unpickled.

Note that registering a type requires implicit pickler and unpickler for that
type. Implicit picklers and unpicklers can be materialized automatically via
macros for case classes. More on this later.

For values, a `SingletonFullName[A]` is required. Singleton full names can be
materialized automatically via macros for case objects.

### Pickle builders and readers

Pickle builders and readers abstract away the actual format of pickles. Not too
much, though: they assume some kind of JSON-ish structure. Their definitions
are:

```scala
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
```

The packages `org.scalajs.spickling.jsany` and `org.scalajs.spickling.playjson`
provide implicit pickle builders and readers for `js.Any` in Scala.js, and
`JsValue` in Play! JSON, respectively. You may define your own if you want to
work with a different implementation of JSON.

### Picklers and unpicklers

The actual pickling work is done by type-aware picklers an unpicklers. Their
definitions are simple:

```scala
trait Pickler[A] {
  def pickle[P](obj: A)(implicit registry: PicklerRegistry,
      builder: PBuilder[P]): P
}

trait Unpickler[A] {
  def unpickle[P](pickle: P)(implicit registry: PicklerRegistry,
      reader: PReader[P]): A
}
```

The library provides implicit picklers and unpicklers for all primitive types
of Scala, as well as `String`. There are also implicit macros that can
materialize picklers and unpicklers automatically for case classes. These will
pickle recursively the parameters of the case class (in the first parameter
list of the constructor). Other fields will not be pickled by the automatic
picklers.

You can define custom picklers for your data types simply by implementing these
two interfaces, and registering them to a pickler registry.

## License

Scala.js Pickling is distributed under the
[Scala License](http://www.scala-lang.org/license.html).
