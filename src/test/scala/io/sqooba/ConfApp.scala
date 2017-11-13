package io.sqooba

import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.Logger
import io.sqooba.conf.SqConf

object ConfApp extends App {

  val logger = Logger(this.getClass)

  val conf = new SqConf()
  val text = conf.getString("conf.text")
  logger.info(s"conf.text: $text")
  val value = ConfigFactory.load().getString("my.secret.value")
  logger.info(s"my.secret.value: $value")
  logger.info(s"args: ${args.toList}")
}
