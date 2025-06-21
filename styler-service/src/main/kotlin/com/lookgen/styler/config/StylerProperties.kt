package com.lookgen.styler.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "styler")
class StylerProperties {
    var pollMs: Long = 60_000
}
