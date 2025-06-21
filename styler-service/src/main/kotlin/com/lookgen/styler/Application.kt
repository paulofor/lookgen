package com.lookgen.styler

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@ConfigurationPropertiesScan("com.lookgen.styler.config")
class StylerApplication

fun main(args: Array<String>) = runApplication<StylerApplication>(*args)
