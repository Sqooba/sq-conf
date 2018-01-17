package io.sqooba.conf

import org.scalatest.{FlatSpec, Matchers}

class SubConfSpec extends FlatSpec with Matchers {

	val conf = new SqConf

	"get sub config" should "give a valid sqConf" in {
		val childConf = conf.getConfig("subConf.another")
		childConf shouldBe a [SqConf]
		val stringVal = childConf.getString("aString")
		stringVal shouldBe "this is a string"
	}

	"sub config" should "give values with short key" in {
		val childConf = conf.getConfig("subConf.another")
		val stringVal = childConf.getString("aString")
		stringVal shouldBe "this is a string"
	}

}
