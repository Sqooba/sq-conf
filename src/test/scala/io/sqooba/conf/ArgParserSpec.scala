package io.sqooba.conf

import org.scalatest.{FlatSpec, Matchers}

class ArgParserSpec extends FlatSpec with Matchers {

  "ArgParser" should "parse arguments to key-value pairs" in {
    val args = Array("--logFilePath=/somepath", "--date=2009-12-01")
    val asMap = ArgParser.parse(args)
    asMap("logFilePath") shouldBe "/somepath"
    asMap("date") shouldBe "2009-12-01"
  }

  "parsing error" should "print a warning and ignore the key in question" in {
    val args = Array("--logFilePath=/somepath", "--date=2009-12-01", "noprefixnorequals")
    val asMap = ArgParser.parse(args)
    asMap.contains("logFilePath") shouldBe true
    asMap.size shouldBe 2
  }
}
