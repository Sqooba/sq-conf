package io.sqooba.conf

import java.util

import org.scalatest.{FlatSpec, Matchers}

class JavaWrapperSpec extends FlatSpec with Matchers {

	val javaWrapper = new SqConf().asJava()

	"java wrapper" should "return java string" in {
		val prop  = javaWrapper.getString("some.testStringValue")
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

	"get list of ints" should "return valid java list" in {
		val listOf = javaWrapper.getListOf[Integer]("some.testIntListValue")
		listOf.size() shouldBe 3
		listOf.get(0) shouldBe 123
		listOf.get(1) shouldBe 23
		listOf.get(2) shouldBe 69
	}

	"get string as integer using transformer" should "give integer" in {
		val transformer = new StringToIntTransformer
		val result = javaWrapper.getWithTransformer[Integer]("some.testIntegerAsString", transformer)
		result shouldBe a [java.lang.Integer]
		result shouldBe 150
	}

	"get string as list of integers using transformer" should "give list of integers" in {
		val transformer = new StringToIntTransformer
		val result = javaWrapper.getListWithTransformer[Integer]("some.testIntegersAsStringList", transformer)
		result.length shouldBe 3
		result.head shouldBe 150
    result.last shouldBe 350
	}

	"java wrapper with overrides" should "give overide values" in {
		val or: java.util.Map[java.lang.String, java.lang.String] = new util.HashMap[String, String]()
    or.put("some.testStringValue", "new value")
    val javaOrWrapper = new SqConf().asJava().withOverrides(or)
    javaOrWrapper.getString("some.testStringValue") shouldBe "new value"
	}
}
