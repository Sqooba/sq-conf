package io.sqooba.conf

import org.scalatest.{FlatSpec, Matchers}

class DotEnvVariableSpec extends FlatSpec with Matchers {

	val conf = new SqConf()

	"read int from env" should "refer env variable to conf" in {
		EnvUtil.setEnv("some.testIntValue", "50")
		val prop = conf.getInt("some.testIntValue")
		prop shouldBe a [java.lang.Integer]   // does not work with scala.Int for some reason
		prop shouldBe 50
		EnvUtil.removeEnv("some.testIntValue")
	}

	"reading a list fo ints from env-variable" should "create a list of integers in config" in {
		EnvUtil.setEnv(conf.keyAsEnv("some.testIntValue"), "1,2,3")
		val props = conf.getListOfInt("some.testIntValue")
		props shouldBe a [List[_]]   // does not work with scala.Int for some reason
		props.length shouldBe 3
		props(0) shouldBe 1
		props(1) shouldBe 2
		props(2) shouldBe 3
		EnvUtil.removeEnv("some.testIntValue")
	}
}
// some cases parameters come as a map, sq-conf does not work well with this
