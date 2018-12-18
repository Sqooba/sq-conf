package io.sqooba.conf

import java.util

class JavaSqConf(sqConf: SqConf) {

	def getSqConf: SqConf = sqConf

	def getString(key: String): java.lang.String = sqConf.getString(key)

	def getInt(key: String): java.lang.Integer = sqConf.getInt(key)

	def getBoolean(key: String): java.lang.Boolean = sqConf.getBoolean(key)

	def getDuration(key: String): java.time.Duration = sqConf.getDuration(key)

	def get[T](key: String): T = sqConf.get[T](key)

	def getIterable[T](key: String): java.lang.Iterable[T] = {
		val list: List[T] = sqConf.getListOf[T](key)
		import scala.collection.JavaConverters._
		list.asJava
	}

  def getListOf[T](key: String): util.List[T] = IterToList.fromIterToList(getIterable[T](key))

	def keyAsEnv(key: String): String = sqConf.keyAsEnv(key)

	def withOverwrites(ow: java.util.Map[java.lang.String, java.lang.String]): JavaSqConf = {
    val sMap = ow.keySet().toArray().map(key => {
      (key.toString, ow.get(key))
    }).toMap
    sqConf.withOverwrites(sMap).asJava()
  }
}
