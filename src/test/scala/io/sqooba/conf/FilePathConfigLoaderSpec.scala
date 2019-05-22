package io.sqooba.conf

import java.io.File

import com.typesafe.config.ConfigFactory
import org.scalatest.{FlatSpec, Matchers}

class FilePathConfigLoaderSpec extends FlatSpec with Matchers {

	"loading from path" should "create a valid config" in {
		val f = new File("src/test/resources/another.conf")
		f.exists shouldBe true
		f.isFile shouldBe true
		val sq = SqConf.forFile(f)

		sq.getBoolean("this.has.conf") shouldBe true
	}

	"wrap a config" should "work" in {
		val f = new File("src/test/resources/another.conf")
		val conf = ConfigFactory.parseFile(f)

		val sq = SqConf.forConfig(conf)
		sq.getBoolean("this.has.conf") shouldBe true
	}
}
