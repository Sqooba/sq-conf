package io.sqooba.conf

import java.time.Duration

import com.typesafe.config.ConfigException
import org.scalatest.{FlatSpec, Matchers}

class SqConfSpec extends FlatSpec with Matchers {

  val conf: SqConf = new SqConf
  val anotherConf: SqConf = SqConf.forFilename("another.conf")

  val overWrite = Map("some.testIntValue" -> "15")
  val anotherConfWithOverrides: SqConf = SqConf.forFilename("another.conf").withOverwrites(overWrite)

  "convert conf path" should "uppercase it properly" in {
    val uppercased = conf.keyAsEnv("some.testIntValue")
    uppercased shouldBe "SOME_TESTINTVALUE"
  }

  "read int from conf" should "get an int from conf" in {
    EnvUtil.removeEnv(conf.keyAsEnv("some.testIntValue"))
    val prop = conf.getInt("some.testIntValue")
    prop shouldBe a [java.lang.Integer]   // does not work with scala.Int for some reason
    prop shouldBe 187
  }

  "read string from conf" should "get a string from conf" in {
    EnvUtil.removeEnv(conf.keyAsEnv("some.testStringValue"))
    val prop = conf.getString("some.testStringValue")
    prop shouldBe a [String]
    prop shouldBe "string thing"
  }

  "read big int from conf" should "get a string from conf" in {
    val prop = conf.getBigInt("some.testBigIntValue")
    prop shouldBe a [BigInt]
    prop shouldBe BigInt(123456789)
  }

  "another conf" should "have value" in {
    val prop = anotherConf.getBoolean("this.has.conf")
    prop shouldBe true
  }

  "another conf with overwrites" should "have a different value" in {
    val prop1 = anotherConf.getInt("some.testIntValue")
    val prop2 = anotherConfWithOverrides.getInt("some.testIntValue")
    prop2 shouldBe 15
    prop1 should not be prop2
    prop1 shouldBe 100
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
    EnvUtil.removeEnv(conf.keyAsEnv("some.testIntListValue"))
    val stringArray = conf.getListOf[Int]("some.testIntListValue")
    stringArray.forall(_.isInstanceOf[Int]) shouldBe true
    stringArray.length shouldBe 3
  }

  "get duration" should "give duration" in {
    val duration: Duration = conf.getDuration("some.testDurationValue")
    duration shouldBe Duration.ofMinutes(10)
  }

  "get t" should "return parameterized type" in {
    val intProp = conf.get[Int]("some.testIntValue")
    intProp shouldBe a [java.lang.Integer] // does not work with scala.Int for some reason
    intProp shouldBe 187

    val stringProp = conf.get[String]("some.testStringValue")
    stringProp shouldBe a [String]
    stringProp shouldBe "string thing"
  }

  "non existent key" should "throw exception" in {
    val thrown = intercept[ConfigException] {
      conf.getString("this.string.does.not.exist")
    }
    thrown.getMessage should include ("No configuration setting found for key")
  }

  "get duration list" should "give duration" in {
    EnvUtil.removeEnv(conf.keyAsEnv("some.testDurationListValue"))
    val duration: List[Duration] = conf.getListOfDuration("some.testDurationListValue")

    val firstDuration: Duration = duration.head
    val tenMinDuration: Duration = Duration.ofMinutes(10)

    firstDuration shouldBe tenMinDuration
  }
}
