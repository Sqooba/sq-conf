package io.sqooba.conf

import java.util.Properties

import org.scalatest.{FlatSpec, Matchers}

class PropertiesSpec extends FlatSpec with Matchers {

  val conf: SqConf = new SqConf

  "sq conf to properties" should "give java.util.Properties" in {
    val props = conf.toProperties()
    val intVal = conf.get[Int]("some.testIntValue")
    val stringVal = conf.get[String]("some.testStringValue")

    props shouldBe a [Properties]
    val prop = props.getProperty("some.testStringValue")

    stringVal shouldBe props.getProperty("some.testStringValue")
  }

}
