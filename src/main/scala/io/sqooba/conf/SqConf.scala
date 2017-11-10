package io.sqooba.conf


import scala.util.Properties

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

class SqConf(fileName: String = "application.conf") {

  val conf: Config = {
    ConfigFactory.load(fileName)
  }

  def getInt(key: String): Int = {
    Properties.envOrNone(keyAsEnv(key)) match {
      case Some(env) => env.toInt
      case None =>  conf.getInt(key)
    }
  }

  def getString(key: String): String = Properties.envOrElse(keyAsEnv(key), conf.getString(key))

  def getBoolean(key: String): Boolean = {
    Properties.envOrNone(keyAsEnv(key)) match {
      case Some(env) => env.toBoolean
      case None =>  conf.getBoolean(key)
    }
  }

  def keyAsEnv(key: String): String = key.toUpperCase.replaceAll("""\.""", "_")
}
