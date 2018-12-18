package io.sqooba.conf

import java.time.Duration

import org.scalatest.{FlatSpec, Matchers}

class EnvOverwritesSpec extends FlatSpec with Matchers {

	 // testIntListValue = [123, 23, 69]
	 val conf = new SqConf

	"read int from env" should "refer env variable to conf" in {
		EnvUtil.setEnv(conf.keyAsEnv("some.testIntValue"), "50")
		val prop = conf.getInt("some.testIntValue")
		prop shouldBe a [java.lang.Integer]   // does not work with scala.Int for some reason
		prop shouldBe 50
		EnvUtil.removeEnv(conf.keyAsEnv("some.testIntValue"))
	}

	"read string from env" should "prefer env variable to conf" in {
		EnvUtil.setEnv(conf.keyAsEnv("some.testStringValue"), "newValue")
		val prop = conf.getString("some.testStringValue")
		prop shouldBe a [String]
		prop shouldBe "newValue"
		EnvUtil.removeEnv(conf.keyAsEnv("some.testStringValue"))
	}

	"read int type array" should "give a list of ints" in {
		EnvUtil.setEnv(conf.keyAsEnv("some.testIntListValue"), "1 ,2 , 3")
		val intArray = conf.getListOfInt("some.testIntListValue")
		intArray.forall(_.isInstanceOf[Int]) shouldBe true
		intArray.length shouldBe 3
		intArray should contain allOf (1,2,3)
		EnvUtil.removeEnv(conf.keyAsEnv("some.testIntListValue"))
	}

	"read string type array" should "give a list of string" in {
		EnvUtil.setEnv(conf.keyAsEnv("some.testStringListValue"), "first ,second , third")
		val stringArray = conf.getListOfString("some.testStringListValue")
		stringArray.forall(_.isInstanceOf[String]) shouldBe true
		stringArray.length shouldBe 3
		stringArray should contain allOf ("first","second","third")
		EnvUtil.removeEnv(conf.keyAsEnv("some.testStringListValue"))
	}

	"read boolean type array" should "give a list of booleans" in {
		EnvUtil.setEnv(conf.keyAsEnv("some.testBooleanListValue"), "false, true, false")
		val booleanArray = conf.getListOfBoolean("some.testBooleanListValue")
		booleanArray.forall(_.isInstanceOf[Boolean]) shouldBe true
		booleanArray.length shouldBe 3
		booleanArray should contain allOf (true, false)
		EnvUtil.removeEnv(conf.keyAsEnv("some.testBooleanListValue"))
	}
	"read duration type array" should "give a list of duration" in {
		// import scala.concurrent.duration._
		EnvUtil.setEnv(conf.keyAsEnv("some.testDurationListValue"), "10m, 5s, 1h")
		val durationArray = conf.getListOfDuration("some.testDurationListValue")
		durationArray.forall(_.isInstanceOf[Duration]) shouldBe true
		durationArray.length shouldBe 3

		durationArray(0) shouldBe Duration.ofMinutes(10)
		durationArray(1) shouldBe Duration.ofSeconds(5)
		durationArray(2) shouldBe Duration.ofHours(1)
		EnvUtil.removeEnv(conf.keyAsEnv("some.testDurationListValue"))
	}
}
