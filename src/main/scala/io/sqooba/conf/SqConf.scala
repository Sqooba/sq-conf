package io.sqooba.conf


import scala.util.Properties

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.Logger

class SqConf(fileName: String = "application.conf") {

  val logger = Logger(this.getClass)

  val conf: Config = ConfigFactory.load(fileName)

  def getInt(key: String): Int = {
    Properties.envOrNone(keyAsEnv(key)) match {
      case Some(env) => env.toInt
      case None => conf.getInt(key)
    }
  }

  def getString(key: String): String = Properties.envOrElse(keyAsEnv(key), conf.getString(key))

  def getBoolean(key: String): Boolean = {
    Properties.envOrNone(keyAsEnv(key)) match {
      case Some(env) => env.toBoolean
      case None => conf.getBoolean(key)
    }
  }

  def keyAsEnv(key: String): String = {
    val asEnvKey = key.toUpperCase.replaceAll("""\.""", "_")
    logger.debug(s"PropertyKey: '$key' is as envKey: '$asEnvKey'")
    asEnvKey
  }
}
