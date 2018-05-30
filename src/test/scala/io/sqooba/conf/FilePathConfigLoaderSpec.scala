package io.sqooba.conf

import java.io.File

import org.scalatest.{FlatSpec, Matchers}

class FilePathConfigLoaderSpec extends FlatSpec with Matchers {
	"load from path" should "work" in {
		val f = new File("src/test/resources/another.conf")
		f.exists() shouldBe true
		f.isFile() shouldBe true
		val sq = SqConf.ofFile(f)

		sq.getBoolean("this.has.conf") shouldBe true
	}
}
