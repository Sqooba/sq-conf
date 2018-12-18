package io.sqooba.conf

import org.scalatest.{FlatSpec, Matchers}

class PropertiesFileSpec extends FlatSpec with Matchers {

  val conf: SqConf = SqConf.forFilename("prop.properties")

  "get int from properties" should "have a valid value" in {
    conf.getInt("testIntVal") shouldBe 15
  }

  "get string from properties" should "have a valid value" in {
    conf.getString("testStringVal") shouldBe "perse"
  }

  "get boolean from properties" should "have a valid value" in {
    conf.getBoolean("testBooleanVal") shouldBe false
  }
}
