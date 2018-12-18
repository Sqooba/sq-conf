package io.sqooba.conf

import org.scalatest.{FlatSpec, Matchers}

class GenGetterSpec extends FlatSpec with Matchers {

  val conf: SqConf = new SqConf
/*
  "use new getter for string" should "return same val" in {
    val prop1 = conf.getString("some.testStringValue")
    val prop2 = conf.getStringNew("some.testStringValue")
    prop1 shouldBe prop2
  }

  "use new getter for int" should "return same val" in {
    val prop1 = conf.getInt("some.testIntValue")
    val prop2 = conf.getIntNew("some.testIntValue")
    prop1 shouldBe prop2
  }

  "use new getter for long" should "return same val" in {
    val long: Long = 100
    val prop = conf.getLongNew("some.testLongValue")
    long shouldBe prop
  }
*/
}
