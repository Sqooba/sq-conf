package io.sqooba.conf

class JavaSqConf(sqConf: SqConf) {

	def getSqConf(): SqConf = sqConf

	def getString(key: String): java.lang.String = sqConf.getString(key)

	def getInt(key: String): java.lang.Integer = sqConf.getInt(key)

	def getBoolean(key: String): java.lang.Boolean = sqConf.getBoolean(key)

	def get[T](key: String): T = sqConf.get[T](key)

	def getIterable[T](key: String): java.lang.Iterable[T] = {
		val list: List[T] = sqConf.getListOf[T](key)
		import scala.collection.JavaConverters._
		list.asJava
	}

	def keyAsEnv(key: String) = sqConf.keyAsEnv(key)
}
