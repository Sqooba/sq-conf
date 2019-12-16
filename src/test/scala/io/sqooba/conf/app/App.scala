package io.sqooba.conf.app

import io.sqooba.conf.SqConf

object App {

	def main(args: Array[String]): Unit = {
		val conf = new SqConf()
		val s = conf.getString("some.testStringValue")
		println(s"test: $s")
		val sConf = conf.getSubConfig("subConf")
		val lKeys = sConf.getListOfKeys
		println(s"lKeys: $lKeys")
	}
}
