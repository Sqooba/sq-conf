package io.sqooba.conf


import scala.util.Properties

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.{LazyLogging}

class SqConf(fileName: Option[String] = None,
             prefix: Option[String] = None,
             valueOverrides: Map[String, String] = Map()) extends LazyLogging {

  def asJava() = new JavaSqConf(this)

  def this() = this(None, None)

  val conf: Config = {
    fileName match {
      case Some(file) => ConfigFactory.load(file)
      case _ => ConfigFactory.load()
    }
  }

  def buildKey(key: String): String = {
    prefix match {
      case Some(pre) => s"$pre.$key"
      case None => key
    }
  }

  def getInt(key: String): Int = {
    val fullKey = buildKey(key)
    if (valueOverrides.contains(fullKey)) {
      valueOverrides(fullKey).toInt
    } else {
      Properties.envOrNone(keyAsEnv(fullKey)) match {
        case Some(env) => env.toInt
        case None => conf.getInt(fullKey)
      }
    }
  }

  def getString(key: String): String = {
    val fullKey = buildKey(key)
    if (valueOverrides.contains(fullKey)) {
      valueOverrides(fullKey)
    } else {
      Properties.envOrElse(keyAsEnv(fullKey), conf.getString(fullKey))
    }
  }

  def getBoolean(key: String): Boolean = {
    val fullKey = buildKey(key)
    if (valueOverrides.contains(fullKey)) {
      valueOverrides(fullKey).toBoolean
    } else {
      Properties.envOrNone(keyAsEnv(fullKey)) match {
        case Some(env) => env.toBoolean
        case None => conf.getBoolean(fullKey)
      }
    }
  }

  def get[T](key: String): T = conf.getAnyRef(key).asInstanceOf[T]

  def keyAsEnv(key: String): String = {
    val asEnvKey = key.toUpperCase.replaceAll("""\.""", "_")
    logger.debug(s"PropertyKey: '$key' is as envKey: '$asEnvKey'")
    asEnvKey
  }

  def getListOf[T](key: String): List[T] = {
    val l = conf.getAnyRefList(key)
    l.toArray.map(x => {x.asInstanceOf[T]}).toList
  }

  def getConfig(confPath: String): SqConf = {
    new SqConf(fileName, Some(confPath))
  }
}
