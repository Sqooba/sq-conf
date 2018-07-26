package io.sqooba.conf

import org.scalatest.{FlatSpec, Matchers}

class JavaWrapperSpec extends FlatSpec with Matchers {

	val javaWrapper = new SqConf().asJava()

	"java wrapper" should "return java string" in {
		val prop: java.lang.String = javaWrapper.getString("some.testStringValue")
		prop shouldBe a [java.lang.String]
	}

	"java wrapper" should "return java int" in {
		EnvUtil.removeEnv(javaWrapper.keyAsEnv("some.testIntValue"))
		val prop: java.lang.Integer = javaWrapper.getInt("some.testIntValue")
		prop shouldBe a [java.lang.Integer]
		prop shouldBe 187
	}

	"java wrapper" should "return java boolean" in {
		val prop: java.lang.Boolean = javaWrapper.getBoolean("some.testBooleanValue")
		prop shouldBe a [java.lang.Boolean]
		prop shouldBe true
	}

	"java wrapper" should "return java string list" in {
		val propList: java.lang.Iterable[String] = javaWrapper.getIterable[String]("some.testStringListValue")
		propList shouldBe a [java.lang.Iterable[_]]
	}

	"java wrapper" should "return java int list" in {
		val propList: java.lang.Iterable[Integer] = javaWrapper.getIterable[Integer]("some.testIntListValue")
		propList shouldBe a [java.lang.Iterable[_]]
	}

	"java wrapper" should "return java duration" in {
		val prop: java.time.Duration = javaWrapper.getDuration("some.testDurationValue")
		prop shouldBe a [java.time.Duration]
		prop.getSeconds shouldBe (10 * 60)
	}

	"parameterized type" should "return t" in {
		val stringy: java.lang.String = javaWrapper.get[java.lang.String]("some.testStringValue")
		stringy shouldBe a [java.lang.String]
		stringy shouldBe "string thing"
	}
}
