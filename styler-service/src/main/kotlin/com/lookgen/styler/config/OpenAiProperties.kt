package com.lookgen.styler.config

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "openai")
class OpenAiProperties {
    lateinit var apiKey: String
    var readTimeout: Duration = Duration.ofSeconds(120)
}
