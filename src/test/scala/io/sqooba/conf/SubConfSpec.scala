package io.sqooba.conf

import org.scalatest.{FlatSpec, Matchers}

class SubConfSpec extends FlatSpec with Matchers {

	val conf: SqConf = new SqConf
	val childConf: SqConf = conf.getSubConfig("subConf.another")

	val javaConf: JavaSqConf = conf.asJava()
	val javaChildConf: JavaSqConf = javaConf.getConfig("subConf.another")


	"get sub config" should "give a valid sqConf" in {
		childConf shouldBe a [SqConf]
    val rootString = conf.getString("subConf.rootString")
		val stringVal = childConf.getString("aString")

    rootString shouldBe "root"
		stringVal shouldBe "this is a string"
	}

	it should "give int with short key" in {
		val intVal = childConf.getInt("aInt")
		intVal shouldBe 123
	}

  "java sub config" should "give string with short key" in {
    val stringVal: java.lang.String = javaChildConf.getString("aString")
    stringVal shouldBe "this is a string"
  }

  it should "give int with short key" in {
    val intVal: java.lang.Integer = javaChildConf.getInt("aInt")
    intVal shouldBe 123
  }

	"get keys" should "give a list of keys at the sub conf level" in {
		val keys = childConf.getListOfKeys
		keys.size shouldBe 2
		keys should contain allOf ("aInt", "aString")
	}

	it should "give a list of keys at the given sub conf level" in {
		val keys = conf.getSubConfig("subConf").getListOfKeys

		keys.size shouldBe 3
		keys should contain allOf ("intList", "another", "rootString")
	}

	"sub conf of a subconf" should "still give valid items" in {
		val sConf = conf.getSubConfig("subConf")
		val ssConf = sConf.getSubConfig("another")

		ssConf.getString("aString") shouldBe "this is a string"
		ssConf.getInt("aInt") shouldBe 123
	}

	"sub conf" should "give list of keys for complex items" in {
		val sConf = conf.getSubConfig("keyConf.sub")
		val setOfKeys = sConf.getListOfKeys
		setOfKeys.size shouldBe 2
		setOfKeys should contain allOf ("complex1", "complex2")
	}
}
