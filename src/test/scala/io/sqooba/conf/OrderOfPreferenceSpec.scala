package io.sqooba.conf

import org.scalatest.{FlatSpec, Matchers}

class OrderOfPreferenceSpec extends FlatSpec with Matchers {

  "default conf" should "have preference: overrides, env, file" in {
    val conf = new SqConf()
    conf.getOrderOfPreference shouldBe List(OrderOfPreference.VALUE_OVERRIDES,
      OrderOfPreference.ENV_VARIABLE,
      OrderOfPreference.CONF_FIlE)
  }

  "conf" should "have configured order preference" in {
    val oop = List(OrderOfPreference.ENV_VARIABLE,
      OrderOfPreference.VALUE_OVERRIDES,
      OrderOfPreference.CONF_FIlE)
    val conf = new SqConf().configureOrder(oop)
    conf.getOrderOfPreference shouldBe oop
  }

  "simple value" should "be given according to default order" in {
    val conf = new SqConf().withOverrides(Map("subConf.rootString" -> "newRoot"))
    val res = conf.getValueAccordingOrderOfOfPreference[String]("subConf.rootString", ((st: String) => st))
    res shouldBe "newRoot"
  }

  "simple value" should "be given according to parameters" in {
    val oop = List(OrderOfPreference.ENV_VARIABLE,
      OrderOfPreference.CONF_FIlE,
      OrderOfPreference.VALUE_OVERRIDES)

    val conf = new SqConf().withOverrides(Map("subConf.rootString" -> "newRoot")).configureOrder(oop)
    val res = conf.getValueAccordingOrderOfOfPreference[String]("subConf.rootString", ((st: String) => st))
    res shouldBe "root"
  }
}
