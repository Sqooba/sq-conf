package io.sqooba.conf

import com.typesafe.scalalogging.LazyLogging

object ArgParser extends LazyLogging {

  def parse(args: Array[String]): Map[String, String] = {
    args.map(param => {
      val parts = param.split("=")
      if (parts.length == 2) {
        (parts(0).stripPrefix("--"),parts(1))
      } else {
        logger.info(s"Can't parse given command line parameter: $param")
        ("","") // temporal empty to match types
      }
    }).filter(!_._1.isEmpty).toMap
  }
}
