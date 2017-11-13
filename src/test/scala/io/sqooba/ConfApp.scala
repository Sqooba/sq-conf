package io.sqooba

import com.typesafe.config.ConfigFactory
import io.sqooba.conf.SqConf

object ConfApp extends App {
  val conf = new SqConf()
  val text = conf.getString("conf.text")
  println(s"conf.text: $text")
  val value = ConfigFactory.load().getString("my.secret.value")
  println(s"my.secret.value: $value")
  println(args.toList)
}
