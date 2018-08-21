package com.typesafe.config.impl
import java.time.Duration

import com.typesafe.config.ConfigOriginFactory

object DurationParser {
	def parseDurationString(duration: String, key: String, source: String): Duration = {
		val nanos = SimpleConfig.parseDuration(duration, ConfigOriginFactory.newSimple("source"), key)
		Duration.ofNanos(nanos)
	}
}
