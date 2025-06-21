package com.lookgen.styler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ConfigurationPropertiesScan("com.lookgen.styler.config")
public class StylerApplication {

    public static void main(String[] args) {
        SpringApplication.run(StylerApplication.class, args);
    }
}
