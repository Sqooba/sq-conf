package io.sqooba.conf

object EnvUtil {

  val field = System.getenv().getClass.getDeclaredField("m")
  field.setAccessible(true)
  val map = field.get(System.getenv()).asInstanceOf[java.util.Map[java.lang.String, java.lang.String]]

  def removeEnv(key: String) = map.remove(key)

  def setEnv(key: String, value: String) = map.put(key, value)
}
