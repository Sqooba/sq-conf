package io.sqooba.conf

import java.io.File
import java.time.Duration
import java.util.Properties

import scala.collection.JavaConverters._
import scala.reflect.ClassTag

import com.typesafe.config._
import com.typesafe.config.impl.DurationParser
import com.typesafe.scalalogging.LazyLogging
import io.sqooba.conf.OrderOfPreference.OrderOfPreference

// Here we have nulls and avoid option on purpose so the java bindings work without scala standard lib
class SqConf(fileName: String = null,
             file: File = null,
             config: Config = null,
             prefix: String = null,
             valueOverrides: Map[String, String] = Map(), orderOfPreference: List[OrderOfPreference] = SqConf.DEFAULT_ORDER_OF_PREFERENCE) extends LazyLogging {

  def this() = this(null, null, null, null)

  def asJava() = new JavaSqConf(this)

  val conf: Config = {
    (fileName, file, config) match {
      case (null, null, conf: Config) => conf
      case (fileN: String, null, null) => {
        if (checkIfYml(fileN)) { // YML Seems to only work if loaded as a file
          val url = this.getClass.getResource("/" + fileN)
          val f = new File(url.getPath)
          ConfigFactory.parseFile(f)
        } else {
          ConfigFactory.load(fileN)
        }
      }
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

  def checkIfYml(fileN: String): Boolean = fileN.endsWith("yml") || fileN.endsWith("yaml")

  def getOrderOfPreference: List[OrderOfPreference] = orderOfPreference

  def getInt(key: String): Int = getValueAccordingOrderOfOfPreference[Int](key, x => x.toInt)

  def getString(key: String): String = getValueAccordingOrderOfOfPreference[String](key, x => x)

  def getBoolean(key: String): Boolean = getValueAccordingOrderOfOfPreference[Boolean](key, x => x.toBoolean)

  def getLong(key: String): Long = getValueAccordingOrderOfOfPreference[Long](key, x => x.toLong)

  def getBigInt(key: String): BigInt = getValueAccordingOrderOfOfPreference[BigInt](key, x => BigInt(x))

  def getDuration(key: String): Duration = {
    val fullKey = buildKey(key)
    if (valueOverrides.contains(fullKey)) {
      DurationParser.parseDurationString(valueOverrides(fullKey), fullKey, "valueOverrides")
    } else {
      System.getenv(keyAsEnv(fullKey)) match {
        case null => conf.getDuration(fullKey)
        case env: String => DurationParser.parseDurationString(env, fullKey, "environmentVariable")
      }
    }
  }

  def getValueAccordingOrderOfOfPreference[T: ClassTag](key: String, converter: String => T): T = {
    var value: T = null.asInstanceOf[T]

    orderOfPreference.takeWhile(oop => {
      value = getValueForOrderOfOfPreferenceItem[T](key, oop, converter)
      value == null
    })
    value
  }

  def getListOfValuesAccordingOrderOfPreference[T: ClassTag](key: String, converter: String => T): List[T] = {
    var values: List[T] = List()

    orderOfPreference.takeWhile(oop => {
      values = getListOfValuesForOrderOfOfPreference[T](key, oop, converter)
      values.isEmpty
    })
    values
  }

  def getListOfValuesForOrderOfOfPreference[T: ClassTag](key: String, oop: OrderOfPreference, converter: String => T): List[T] = {
    val fullKey = buildKey(key)

    def stringToT(string: String): List[T] = string.split(',').map(x => {
      converter(x)
    }).toList

    oop match {
      case OrderOfPreference.ENV_VARIABLE =>
        System.getenv(keyAsEnv(fullKey)) match {
          case null => List()
          case env => stringToT(env)
        }
      case OrderOfPreference.CONF_FIlE => getListOf[T](fullKey, converter, cast = false)
      case OrderOfPreference.VALUE_OVERRIDES => {
        if (valueOverrides.contains(fullKey)) {
          stringToT(valueOverrides(fullKey))
        } else {
          List[T]()
        }
      }
    }
  }

  def getValueForOrderOfOfPreferenceItem[T: ClassTag](key: String, oop: OrderOfPreference, converter: String => T): T = {
    val fullKey = buildKey(key)

    oop match {
      case OrderOfPreference.ENV_VARIABLE =>
        System.getenv(keyAsEnv(fullKey)) match {
          case null => null.asInstanceOf[T]
          case env => converter(env)
        }
      case OrderOfPreference.CONF_FIlE => converter(conf.getString(fullKey))
      case OrderOfPreference.VALUE_OVERRIDES => {
        if (valueOverrides.contains(fullKey)) {
          converter(valueOverrides(fullKey))
        } else {
          null.asInstanceOf[T]
        }
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

  def getListOf[T](key: String): List[T] = getListOf[T](key, null, cast = true)

  def getListOfInt(key: String): List[Int] = getListOfValuesAccordingOrderOfPreference[Int](key, str => str.trim.toInt)

  def getListOfDouble(key: String): List[Double] =
    getListOfValuesAccordingOrderOfPreference[Double](key, str => str.trim.toDouble)

  def getListOfLong(key: String): List[Long] =
    getListOfValuesAccordingOrderOfPreference[Long](key, str => str.trim.toLong)

  def getListOfString(key: String): List[String] = getListOfValuesAccordingOrderOfPreference[String](key, str => str.trim)

  def getListOfBoolean(key: String): List[Boolean] =
    getListOfValuesAccordingOrderOfPreference[Boolean](key, str => str.trim.toBoolean)

  def getListOfDuration(key: String): List[Duration] =
    getListOfValuesAccordingOrderOfPreference[Duration](key, str => DurationParser.parseDurationString(str, key, "listOfDuration"))

  def toProperties(defaults: Properties = null): Properties = {
    val props = new Properties
    conf.entrySet().asScala.toList.foreach(keyVal => {
      props.put(keyVal.getKey, keyVal.getValue.unwrapped().toString)
    })
    props
  }

  def getSubConfig(confPath: String): SqConf =
    new SqConf(null, null, config, confPath, valueOverrides, orderOfPreference)

  def withOverrides(overrides: Map[String, String]): SqConf =
    new SqConf(null, null, config, prefix, appendPrefixToOverridesIfNecessary(prefix, overrides), orderOfPreference)

  def appendPrefixToOverridesIfNecessary(prefix: String, overrides: Map[String, String]): Map[String, String] = {
    if (prefix == null || overrides.head._1.contains(prefix)) {
      overrides
    } else {
      overrides.map(kv => {
        (s"$prefix.${kv._1}", kv._2)
      })
    }
  }

  def configureOrder(order: List[OrderOfPreference]): SqConf =
    new SqConf(null, null, config, prefix, valueOverrides, order)
}

object OrderOfPreference extends Enumeration {
  type OrderOfPreference = Value
  val VALUE_OVERRIDES, ENV_VARIABLE, CONF_FIlE = Value
}

object SqConf {

  val DEFAULT_ORDER_OF_PREFERENCE: List[OrderOfPreference] = List(
    OrderOfPreference.VALUE_OVERRIDES,
    OrderOfPreference.ENV_VARIABLE,
    OrderOfPreference.CONF_FIlE)

  def forFile(file: File): SqConf = {
    new SqConf(null, file, null, null)
  }

  def forConfig(config: Config): SqConf = {
    new SqConf(null, null, config, null)
  }

  def forFilename(fileName: String): SqConf = {
    new SqConf(fileName, null, null, null)
  }

  def default(): SqConf = {
    new SqConf(null, null, null, null)
  }
}