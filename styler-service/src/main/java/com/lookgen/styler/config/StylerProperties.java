package com.lookgen.styler.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "styler")
public class StylerProperties {

    private long pollMs = 60_000L;
    private String imageBaseUrl;

    public long getPollMs() {
        return pollMs;
    }

    public void setPollMs(long pollMs) {
        this.pollMs = pollMs;
    }

    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public void setImageBaseUrl(String imageBaseUrl) {
        this.imageBaseUrl = imageBaseUrl;
    }
}
