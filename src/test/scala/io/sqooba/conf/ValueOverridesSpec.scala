package io.sqooba.conf

import java.time.Duration

import org.scalatest.{FlatSpec, Matchers}

class ValueOverridesSpec extends FlatSpec with Matchers {

  val overrideMap: Map[String, String] = Map("some.testIntValue" -> "15",
    "some.testStringValue" -> "overridenstring",
    "some.testBooleanValue" -> "false",
    "some.testDurationValue" -> "10 s",
    "some.testStringListValue" -> "first,second,third",
    "some.durationListValue" -> "10s, 10m")

  val sqConf: SqConf = new SqConf().withOverrides(overrides = overrideMap)

  "overwritten int" should "return value that matches the one in the map" in {
    sqConf.getInt("some.testIntValue") shouldBe 15
  }

  "overwritten string" should "return value that matches the one in the map" in {
    sqConf.getString("some.testStringValue") shouldBe "overridenstring"
  }

  "overwritten boolean" should "return value that matches the one in the map" in {
    sqConf.getBoolean("some.testBooleanValue") shouldBe false
  }

  "overriden duraion" should "" in {
    sqConf.getDuration("some.testDurationValue") shouldBe Duration.ofSeconds(10)
  }

  "overwritten duration list" should "return duration that matches the one in the map" in {
    val timeList: List[Duration] = sqConf.getListOfDuration("some.durationListValue")
    timeList.head shouldBe Duration.ofSeconds(10)
    timeList.last shouldBe Duration.ofMinutes(10)
    timeList.forall(d => d.isInstanceOf[Duration]) shouldBe true
  }
}

