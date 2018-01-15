package io.sqooba.conf


import scala.util.Properties

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.Logger

class SqConf(fileName: String = "application.conf", prefix: Option[String] = None) {

  val logger = Logger(this.getClass)

  val conf: Config = ConfigFactory.load(fileName)

  def buildKey(key: String): String = {
    prefix match {
      case Some(pre) => s"$pre.$key"
      case None => key
    }
  }

  def getInt(key: String): Int = {
    val fullKey = buildKey(key)
    Properties.envOrNone(keyAsEnv(fullKey)) match {
      case Some(env) => env.toInt
      case None => conf.getInt(fullKey)
    }
  }

  def getString(key: String): String = {
    val fullKey = buildKey(key)
    Properties.envOrElse(keyAsEnv(fullKey), conf.getString(fullKey))
  }

  def getBoolean(key: String): Boolean = {
    val fullKey = buildKey(key)
    Properties.envOrNone(keyAsEnv(fullKey)) match {
      case Some(env) => env.toBoolean
      case None => conf.getBoolean(fullKey)
    }
  }

  def keyAsEnv(key: String): String = {
    val asEnvKey = key.toUpperCase.replaceAll("""\.""", "_")
    logger.debug(s"PropertyKey: '$key' is as envKey: '$asEnvKey'")
    asEnvKey
  }

  def getConfig(confPath: String): SqConf = {
    new SqConf(fileName, Some(confPath))
  }
}
