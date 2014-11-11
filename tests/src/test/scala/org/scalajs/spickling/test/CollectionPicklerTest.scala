package org.scalajs.spickling
package test

import java.util.Date

import scala.reflect.{ ClassTag, classTag }

import scala.scalajs.js
import scala.scalajs.test.JasmineTest
import org.scalajs.jasmine._
import org.scalajs.spickling.jsany._


object CollectionPicklerTest  extends PicklersTest {
  self=>

  def testPickling[T](value: T): Unit = {
    val p = PicklerRegistry.pickle(value)
    expect(PicklerRegistry.unpickle(p).asInstanceOf[js.Any]).toEqual(value.asInstanceOf[js.Any])
  }


  describe("Collection picklers") {


    it("should be able to (un)pickle a Map"){

      testPickling(Map("hello"->"world","goodbye"->"Java"))
      testPickling(Map("one"->1,"two"->2,"three"->3))
      testPickling(Map("one"->Map("one"->11,"two"->12,"three"->13),"two"->Map("one"->21,"two"->22,"three"->23),"three"->Map("one"->31,"two"->32,"three"->33)))

    }

    it("should be able to (un)pickle Tuples"){
      testPickling(("one","two"))
      testPickling(("one","two","three"))
      testPickling((1,2,3,4))
      testPickling( ( "one"->"two","three"->"four" ) )

    }

    it("should be able to (un)pickle Vectors"){
      testPickling(Vector(1,2,3,4))
      testPickling(Vector("one","two"))
      testPickling(Vector(Vector(1,2,3,4),Vector(5,6,7,8)))
    }

    it("should be able to (un)pickle Sets"){
      testPickling(Set(1,2,3,4))
      testPickling(Set("one","two"))
      testPickling(Set(Set(1,2,3,4),Set(5,6,7,8)))
    }

    it("should be able to (un)pickle Dates"){
      testPickling(new Date(2014,10,4))
    }

    it("should be able to (un)pickle Lists"){
      testPickling(List(1,2,3))
      testPickling(List(List(1,2)),List(3,4))
    }

    it("should be able to (un)pickle Options"){
      testPickling(Some(10))
      testPickling(None)
      testPickling(Some((10,Some(23),None)))

    }



  }
}