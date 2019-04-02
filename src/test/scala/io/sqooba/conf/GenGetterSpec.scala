package io.sqooba.conf

import org.scalatest.{FlatSpec, Matchers}

class GenGetterSpec extends FlatSpec with Matchers {

  val conf: SqConf = new SqConf


  "getting int" should "give valid int if value is right" in {
    val intVal = conf.get[Int]("some.testIntValue")
    intVal shouldBe a [java.lang.Integer]
    intVal shouldBe 187
  }

  "getting string" should "give valid int if value is right" in {
    val stringVal = conf.get[String]("some.testStringValue")
    stringVal shouldBe a [String]
    stringVal shouldBe "string thing"
  }

  "getting long" should "give valid int if value is right" in {
    val stringVal = conf.get[String]("some.testStringValue")
    stringVal shouldBe a [String]
    stringVal shouldBe "string thing"
  }

  "getting string list" should "give valid int if value is right" in {
    val stringList = conf.getListOf[List[String]]("some.testStringListValue")
    stringList shouldBe a [List[_]]
    stringList shouldBe List("some","list", "last")
  }
}
