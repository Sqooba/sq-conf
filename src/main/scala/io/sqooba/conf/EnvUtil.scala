package io.sqooba.conf

import java.lang.reflect.Field
import java.util

object EnvUtil {

  val field: Field = System.getenv().getClass.getDeclaredField("m")
  field.setAccessible(true)
  val map: util.Map[String, String] = field.get(System.getenv()).asInstanceOf[java.util.Map[java.lang.String, java.lang.String]]

  def removeEnv(key: String): String = map.remove(key)

  def setEnv(key: String, value: String): String = map.put(key, value)
}
