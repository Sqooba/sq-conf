package io.sqooba.conf

import org.scalatest.{FlatSpec, Matchers}

class DotEnvVariableSpec extends FlatSpec with Matchers {

	val conf = new SqConf()

	"read int from env" should "refer env variable to conf" in {
		EnvUtil.setEnv(conf.keyAsEnv("some.testIntValue"), "50")
		val prop = conf.getInt("some.testIntValue")
		prop shouldBe a [java.lang.Integer]   // does not work with scala.Int for some reason
		prop shouldBe 50
	}

}
// some cases parameters come as a map, sqconf does not work well with this
