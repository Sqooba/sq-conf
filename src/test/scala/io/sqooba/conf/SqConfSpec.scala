package io.sqooba.conf

import java.time.Duration

import com.typesafe.config.{ConfigException, ConfigOriginFactory}
import org.scalatest.{FlatSpec, Matchers}

class SqConfSpec extends FlatSpec with Matchers {

  val conf: SqConf = new SqConf
  val anotherConf: SqConf = SqConf.forFilename("another.conf")

  val overWrite = Map("some.testIntValue" -> "15")
  val anotherConfWithOverrides: SqConf = SqConf.forFilename("another.conf").withOverrides(overWrite)

  "convert conf path" should "uppercase it properly" in {
    val upperCased = conf.keyAsEnv("some.testIntValue")
    upperCased shouldBe "SOME_TESTINTVALUE"
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

	"read string option from conf" should "get a string from conf" in {
		val prop = conf.getStringOption("some.testStringValue")
		prop shouldBe defined
		prop.get shouldBe "string thing"
	}

	"read string from conf" should "return default value if key does not exist" in {
		val prop = conf.getString("some.not.existing.testStringValue", "defaultString")
		prop shouldBe "defaultString"
	}

	//   testIntValue = 187
	"read int from conf" should "get a int from conf" in {
		val prop = conf.getInt("some.testIntValue")
		prop shouldBe 187
	}

	"read int from conf" should "get default if key does not exist" in {
		val prop = conf.getInt("some.test.not.there.IntValue", 69)
		prop shouldBe 69
	}

	"read int from conf" should "get int option" in {
		val prop = conf.getIntOption("some.testIntValue")
		prop shouldBe defined
		prop.get shouldBe 187
	}


	"read big int from conf" should "get a BigInt from conf" in {
    val prop = conf.getBigInt("some.testBigIntValue")
    prop shouldBe a [BigInt]
    prop shouldBe BigInt(123456789)
  }

	"read big int as Option from conf" should "get a Option[BigInt] from conf" in {
		val prop = conf.getBigIntOption("some.testBigIntValue")
		prop shouldBe defined
		prop.get shouldBe a [BigInt]
		prop.get shouldBe BigInt(123456789)
	}

	"read non existing big int as Option from conf" should "get a None from conf" in {
		val prop = conf.getBigIntOption("some.testNonExistentBigIntValue")
		prop should not be defined
		prop shouldBe None
	}

	"read non existing big int as default from conf" should "get a def from conf" in {
		val prop = conf.getBigInt("some.testNonExistentBigIntValue", BigInt(100))
		prop shouldBe BigInt(100)
	}

  "read long from conf" should "get a long from conf" in {
    val prop = conf.getLong("some.testLong2Value")
    prop shouldBe a [java.lang.Long]
    prop shouldBe 9223372036854775807L
  }

	"read optional long from conf" should "get a long from conf" in {
		val prop = conf.getLongOption("some.testLong2Value")
		prop shouldBe defined
		prop.get shouldBe a [java.lang.Long]
		prop.get shouldBe 9223372036854775807L
	}

	"read default long from conf" should "get a long from conf" in {
		val prop = conf.getLong("some.testNotExistingLongValue", 100L)
		prop shouldBe a [java.lang.Long]
		prop shouldBe 100L
	}

  "another conf" should "have value" in {
    val prop = anotherConf.getBoolean("this.has.conf")
    prop shouldBe true
  }

	"get boolean" should "have value def value" in {
		val prop = anotherConf.getBoolean("this.does.not.exist", false)
		prop shouldBe false
	}

	"get boolean option" should "return None for non existing value" in {
		val prop = conf.getBooleanOption("this.does.not.exist")
		prop shouldBe None
	}

	"get boolean option" should "return Some(boolean) for existing value" in {
		val prop = conf.getBooleanOption("some.testBooleanValue")
		prop shouldBe defined
		prop.get shouldBe true
	}

  "another conf with overwrites" should "have a different value" in {
    EnvUtil.removeEnv(conf.keyAsEnv("some.testIntValue"))
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

	"get duration" should "give duration as default" in {
		val d = Duration.ofMinutes(15)
		val duration: Duration = conf.getDuration("some.testNonExistingDurationValue", d)
		duration shouldBe d
	}

	"get duration" should "give None duration  if key does not exist" in {
		conf.getDurationOption("some.testNonExistingDurationValue") should not be defined
	}

  "get t" should "return parameterized type" in {
		val intProp = conf.get[Int]("some.testIntValue")
		intProp shouldBe a [java.lang.Integer] // does not work with scala.Int for some reason
		intProp shouldBe 187

		val stringProp = conf.get[String]("some.testStringValue")
		stringProp shouldBe a [String]
		stringProp shouldBe "string thing"
	}

	"get t" should "return default value of type" in {
		val intProp = conf.get[Int]("some.testNotThereIntValue", 199)
		intProp shouldBe a [java.lang.Integer] // does not work with scala.Int for some reason
		intProp shouldBe 199

		val stringProp = conf.get[String]("some.testNotThereIStringValue", "this is res")
		stringProp shouldBe a [String]
		stringProp shouldBe "this is res"
	}

	"get option of t" should "return parameterized type wrapped in Option" in {
		val intProp = conf.getOption[Int]("some.testIntValue")
		intProp.get shouldBe a [java.lang.Integer] // does not work with scala.Int for some reason
		intProp.get shouldBe 187

		val stringProp = conf.getOption[String]("some.nontexisting")
		stringProp shouldBe None
	}

  "non existent key" should "throw exception" in {
    val thrown = intercept[ConfigException] {
      conf.getString("this.string.does.not.exist")
    }
    thrown.getMessage should include ("No configuration setting found for key")
  }

  "get list of doubles" should "give a correct list" in {
    val res = conf.getListOfDouble("some.testDoubleListValue")

    res.length shouldBe 3
    compare(res.head, 10.5, 0.00001) shouldBe true
    compare(res(2), 999.999, 0.00001) shouldBe true
  }

  "get list of doubles as strings" should "give a correct list" in {
    val res = conf.getListOfDouble("some.testDoubleListAsStringList")

    res.length shouldBe 3
    compare(res.head, 10.5, 0.00001) shouldBe true
    compare(res(2), 999.999, 0.00001) shouldBe true
  }

  "get duration list" should "give duration" in {
    EnvUtil.removeEnv(conf.keyAsEnv("some.testDurationListValue"))
    val duration: List[Duration] = conf.getListOfDuration("some.testDurationListValue")

    val firstDuration: Duration = duration.head
    val tenMinDuration: Duration = Duration.ofMinutes(10)

    firstDuration shouldBe tenMinDuration
  }

  "get long from strings" should "give a proper long list" in {
    val loooongs: List[Long] = conf.getListOfLong("some.testLongListAsStringValue")
    loooongs.size shouldBe 3
    loooongs.head shouldBe a [java.lang.Long]
  }

  "get ints from strings" should "give a proper int list" in {
    val loooongs: List[Int] = conf.getListOfInt("some.testIntListAsStringValue")
    loooongs.size shouldBe 3
    loooongs.head shouldBe a [java.lang.Integer]
  }

  "get keys" should "give a list of keys at the root of the conf" in {
    val keys = conf.getListOfKeys
    keys.size shouldBe 5
    keys should contain allOf ("simplevalue", "filename", "subConf", "some", "keyConf")
  }

  "get keys" should "give a list of keys for key" in {
    val keys = conf.getListOfKeys("subConf")
    keys.size shouldBe 3
  }

  def compare(x: Double, y: Double, precision: Double): Boolean = {
    if ((x - y).abs < precision) true else false
  }
}
