package io.sqooba.conf

import java.util

class JavaSqConf(sqConf: SqConf) {

  def getSqConf: SqConf = sqConf

  def getString(key: String): java.lang.String = sqConf.getString(key)

  def getInt(key: String): java.lang.Integer = sqConf.getInt(key)

  def getBoolean(key: String): java.lang.Boolean = sqConf.getBoolean(key)

  def getDuration(key: String): java.time.Duration = sqConf.getDuration(key)

  def get[T](key: String): T = sqConf.get[T](key)

  def getWithTransformer[T](key: String, transformer: Transformer[T]): T =
    sqConf.getValueAccordingOrderOfOfPreference(key, transformer.transform(_))

  def getListWithTransformer[T](key: String, transformer: Transformer[T]): List[T] =
    sqConf.getListOf(key, transformer.transform(_), false)

  def getIterable[T](key: String): java.lang.Iterable[T] = {
    val list: List[T] = sqConf.getListOf[T](key)
    import scala.collection.JavaConverters._
    list.asJava
  }

  def getListOf[T](key: String): util.List[T] = IterToList.fromIterToList(getIterable[T](key))

  def keyAsEnv(key: String): String = sqConf.keyAsEnv(key)

  def withOverrides(or: java.util.Map[java.lang.String, java.lang.String]): JavaSqConf = {
    val sMap = or.keySet().toArray().map(key => {
      (key.toString, or.get(key))
    }).toMap
    sqConf.withOverrides(sMap).asJava()
  }

  def toProperties: java.util.Properties = sqConf.toProperties()

  def getConfig(path: String): JavaSqConf = sqConf.getConfig(path).asJava()
}
