package io.sqooba.conf

import java.util.Properties

import org.scalatest.{FlatSpec, Matchers}

class PropertiesSpec extends FlatSpec with Matchers {

  val conf: SqConf = new SqConf

  "sqConf.toProperties" should "create java.util.Properties with valid keys" in {
    val props = conf.toProperties()
    val intVal: Int = conf.get[Int]("some.testIntValue")
    val stringVal = conf.get[String]("some.testStringValue")

    props shouldBe a [Properties]

    intVal shouldBe props.getProperty("some.testIntValue").toInt
    stringVal shouldBe props.getProperty("some.testStringValue")
  }
}
