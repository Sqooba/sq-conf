package io.sqooba.conf

import org.scalatest.{FlatSpec, Matchers}

class SubConfSpec extends FlatSpec with Matchers {

	val conf = new SqConf
	val childConf = conf.getConfig("subConf.another")

	val javaConf = conf.asJava()
	val javaChildConf = javaConf.getConfig("subConf.another")


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

}
