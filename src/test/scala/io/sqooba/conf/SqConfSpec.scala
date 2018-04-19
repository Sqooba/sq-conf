package io.sqooba.conf

import org.scalatest.{FlatSpec, Matchers}

class SqConfSpec extends FlatSpec with Matchers {

  val conf = new SqConf
  val anotherConf = new SqConf(Some("another.conf"))

  "convert conf path" should "uppercase it properly" in {
    val uppercased = conf.keyAsEnv("some.testIntValue")
    uppercased shouldBe "SOME_TESTINTVALUE"
  }

  "read string from conf" should "get a string from conf" in {
    EnvUtil.removeEnv(conf.keyAsEnv("some.testStringValue"))
    val prop = conf.getString("some.testStringValue")
    prop shouldBe a [String]
    prop shouldBe "string thing"
  }

  "read string from env" should "prefer env variable to conf" in {
    EnvUtil.setEnv(conf.keyAsEnv("some.testStringValue"), "newValue")
    val prop = conf.getString("some.testStringValue")
    prop shouldBe a [String]
    prop shouldBe "newValue"
  }

  "read int from conf" should "get an int from conf" in {
    EnvUtil.removeEnv(conf.keyAsEnv("some.testIntValue"))
    val prop = conf.getInt("some.testIntValue")
    prop shouldBe a [java.lang.Integer]   // does not work with scala.Int for some reason
    prop shouldBe 187
  }

  "read int from env" should "refer env variable to conf" in {
    EnvUtil.setEnv(conf.keyAsEnv("some.testIntValue"), "50")
    val prop = conf.getInt("some.testIntValue")
    prop shouldBe a [java.lang.Integer]   // does not work with scala.Int for some reason
    prop shouldBe 50
  }

  "another conf" should "have value" in {
    val prop = anotherConf.getBoolean("this.has.conf")
    prop shouldBe true
  }

  "direct access to typesafe conf" should "work" in {
    val asString = conf.conf.getAnyRef("some.testStringValue").toString
    asString shouldBe "string thing"
  }

  "get t type array" should "give a list of strings" in {
    val stringArray = conf.getListOf[String]("some.testStringListValue")
    stringArray.forall(_.isInstanceOf[String]) shouldBe true
    stringArray.length shouldBe 3
  }

  "get t type array" should "give a list of ints" in {
    val stringArray = conf.getListOf[Int]("some.testIntListValue")
    stringArray.forall(_.isInstanceOf[Int]) shouldBe true
    stringArray.length shouldBe 3
  }

  "get t" should "return parameterized type" in {
    val intprop = conf.get[Int]("some.testIntValue")
    intprop shouldBe a [java.lang.Integer] // does not work with scala.Int for some reason
    intprop shouldBe 187

    val stringprop = conf.get[String]("some.testStringValue")
    stringprop shouldBe a [String]
    stringprop shouldBe "string thing"
  }
}
