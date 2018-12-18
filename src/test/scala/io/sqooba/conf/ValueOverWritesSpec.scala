package io.sqooba.conf

import org.scalatest.{FlatSpec, Matchers}

class ValueOverWritesSpec extends FlatSpec with Matchers {

	val overWrites = Map("some.testIntValue" -> "15",
		"some.testStringValue" -> "overridenstring",
		"some.testBooleanValue" -> "false",
  "some.testStringListValue" -> "first,second,third")

	val sqConf: SqConf = new SqConf(valueOverwrites = overWrites)

	val sqConfBuilder: SqConf = new SqConf().withOverwrites(overWrites)

	"overriden int" should "return value that matches the one in the map" in {
		sqConf.getInt("some.testIntValue") shouldBe 15
    sqConfBuilder.getInt("some.testIntValue") shouldBe 15
	}

	"overriden string" should "return value that matches the one in the map" in {
		sqConf.getString("some.testStringValue") shouldBe "overridenstring"
    sqConfBuilder.getString("some.testStringValue") shouldBe "overridenstring"
	}

	"overriden boolean" should "return value that matches the one in the map" in {
		sqConf.getBoolean("some.testBooleanValue") shouldBe false
    sqConfBuilder.getBoolean("some.testBooleanValue") shouldBe false
	}
}

