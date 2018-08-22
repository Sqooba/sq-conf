package io.sqooba.conf

import org.scalatest.{FlatSpec, Matchers}

class ValueOverridesSpec extends FlatSpec with Matchers {

	val overrides = Map("some.testIntValue" -> "15",
		"some.testStringValue" -> "overridenstring",
		"some.testBooleanValue" -> "false",
  "some.testStringListValue" -> "first,second,third")

	val sqConf: SqConf = new SqConf(valueOverrides = overrides)

	"overriden int" should "return value that matches the one in the map" in {
		sqConf.getInt("some.testIntValue") shouldBe 15
	}

	"overriden string" should "return value that matches the one in the map" in {
		sqConf.getString("some.testStringValue") shouldBe "overridenstring"
	}

	"overriden boolean" should "return value that matches the one in the map" in {
		sqConf.getBoolean("some.testBooleanValue") shouldBe false
	}

  /*
	"overriden int type array" should "give a list of ints" in {
		val stringArray = conf.getListOf[Int]("some.testIntListValue")
		stringArray.forall(_.isInstanceOf[Int]) shouldBe true
		stringArray.length shouldBe 3
	}
  */
}

