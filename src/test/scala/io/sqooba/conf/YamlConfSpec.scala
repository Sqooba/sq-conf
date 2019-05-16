package io.sqooba.conf

import java.io.File

import com.typesafe.config.{Config, ConfigFactory}
import org.scalatest.{FlatSpec, Matchers}

class YamlConfSpec extends FlatSpec with Matchers {


	"create config for yml" should "(filename) work like any else" in {
		val sqConf1 = SqConf.forFilename("another.conf")
		val sqConf2 = SqConf.forFilename("ymlconf.yml")

		sqConf1.getInt("some.testIntValue") shouldBe 100
		sqConf2.getInt("invoice") shouldBe 34843
	}

	"load config set in jvm params" should " work " in {
		val conf = SqConf.default()
		val filename = conf.getString("filename")
		filename shouldBe "this is test application.conf"
	}

	"load config set in jvm params" should " with original work " in {
		val conf: Config = ConfigFactory.load()
		val filename = conf.getString("filename")
		filename shouldBe "this is test application.conf"
	}

	"loading another config" should "be same for typesafe and sq conf" in {
		val sqConf = SqConf.forFilename("another.conf")
		val conf = ConfigFactory.load("another.conf")

		sqConf.getInt("some.testIntValue") shouldBe 100
		conf.getInt("some.testIntValue") shouldBe 100
	}

	"loading another config" should "work for yml, be same for typesafe and sq conf" in {
		val ymlFileName = "ymlconf.yml"
		val u = this.getClass.getResource(s"/$ymlFileName")
		val f  = new File(u.getPath)
		val sqConf = SqConf.forFilename(ymlFileName)
		val conf = ConfigFactory.parseFile(f)

		sqConf.getInt("invoice") shouldBe 34843
		conf.getInt("invoice") shouldBe 34843
	}
}

