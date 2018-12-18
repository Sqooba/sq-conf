package io.sqooba.conf


import java.io.File
import java.time.Duration

import scala.util.Properties
import scala.collection.JavaConverters._

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.typesafe.config.impl.DurationParser
import com.typesafe.scalalogging.LazyLogging

// Here we have nulls and avoid option on purpose so the java bindings work without scala standard lib
class SqConf(fileName: String = null,
             file: File = null,
             config: Config = null,
             prefix: String = null,
             valueOverwrites: Map[String, String] = Map()) extends LazyLogging {

  def this() = this(null, null, null, null)

  def asJava() = new JavaSqConf(this)

  val conf: Config = {
    (fileName, file, config) match {
      case (null, null, conf: Config) => conf
      case (fileN: String, null, null) => ConfigFactory.load(fileN)
      case (_, fileF: File, null) => ConfigFactory.parseFile(fileF)
      case _ => ConfigFactory.load()
    }
  }

  def buildKey(key: String): String = {
    prefix match {
      case null => key
      case pre => s"$pre.$key"
    }
  }

  def getInt(key: String): Int = getValueForKey[Int](key, x => x.toInt)

  def getString(key: String): String = getValueForKey[String](key, x => x)

  def getBoolean(key: String): Boolean = getValueForKey[Boolean](key, x => x.toBoolean)

  def getLong(key: String): Long = getValueForKey[Long](key, x => x.toLong)

  def getBigInt(key: String): BigInt = getValueForKey[BigInt](key, x => BigInt(x))

  def getDuration(key: String): Duration = {
    val fullKey = buildKey(key)
    if (valueOverwrites.contains(fullKey)) {
      DurationParser.parseDurationString(valueOverwrites(fullKey), fullKey, "valueOverrides")
    } else {
      Properties.envOrNone(keyAsEnv(fullKey)) match {
        case Some(env) => DurationParser.parseDurationString(env, fullKey, "environmentVariable")
        case None => conf.getDuration(fullKey)
      }
    }
  }

  def getValueForKey[T](key: String, converter: String => T): T = {
    val fullKey = buildKey(key)
    if (valueOverwrites.contains(fullKey)) {
      converter(valueOverwrites(fullKey))
    } else {
      Properties.envOrNone(keyAsEnv(fullKey)) match {
        case Some(env) => converter(env)
        case None => converter(conf.getString(fullKey))
      }
    }
  }

  def get[T](key: String): T = conf.getAnyRef(key).asInstanceOf[T]

  def keyAsEnv(key: String): String = {
    val asEnvKey = key.toUpperCase.replaceAll("""\.""", "_")
    logger.debug(s"PropertyKey: '$key' is as envKey: '$asEnvKey'")
    asEnvKey
  }

  def getListOf[T](key: String, convert: String => T, cast: Boolean): List[T] = {
    val l = conf.getAnyRefList(key)
    l.asScala.toList.map(x => {
      if (cast) {
        x.asInstanceOf[T]
      } else {
        convert(x.toString)
      }
    })
  }

  def getListOf[T](key: String): List[T] = {
    val l = conf.getAnyRefList(key)
    l.toArray.map(x => {
      x.asInstanceOf[T]
    }).toList
  }

  def getListOfInt(key: String): List[Int] = getListOfWithConversion(key, str => str.trim.toInt)

  def getListOfDouble(key: String): List[Double] = getListOfWithConversion(key, str => str.trim.toDouble)

  def getListOfString(key: String): List[String] = getListOfWithConversion(key, str => str.trim)

  def getListOfBoolean(key: String): List[Boolean] = getListOfWithConversion(key, str => str.trim.toBoolean)

  def getListOfDuration(key: String): List[Duration] = getListOfWithConversion[Duration](key, str =>
    DurationParser.parseDurationString(str, key, "listOfDuration"), cast = false)

  def getListOfWithConversion[T](key: String, convert: String => T, cast: Boolean = true): List[T] = {
    val fullKey = buildKey(key)

    def stringToT(string: String): List[T] = string.split(',').map(x => convert(x)).toList

    if (valueOverwrites.contains(fullKey)) {
      stringToT(valueOverwrites(fullKey))
    } else {
      Properties.envOrNone(keyAsEnv(fullKey)) match {
        case Some(env) => stringToT(env)
        case None => getListOf[T](fullKey, convert, cast)
      }
    }
  }

  def getConfig(confPath: String): SqConf = {
    new SqConf(null, null, config, confPath)
  }

  def withOverwrites(overrides: Map[String, String]): SqConf = {
    new SqConf(null, null, config, prefix, overrides)
  }
}

object SqConf {

  def forFile(file: File): SqConf = {
    new SqConf(null, file, null, null, Map())
  }

  def forConfig(config: Config): SqConf = {
    new SqConf(null, null, config, null, Map())
  }

  def forFilename(fileName: String): SqConf = {
    new SqConf(fileName, null, null, null)
  }

  def default(): SqConf = {
    new SqConf(null, null, null, null)
  }
}