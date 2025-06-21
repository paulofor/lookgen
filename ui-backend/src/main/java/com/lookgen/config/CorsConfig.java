package com.lookgen.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${media.root}")
    private String mediaRoot;

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/api/**")
                .allowedOrigins(
                        "http://localhost:5173",
                        "https://lookgen.online",
                        "https://www.lookgen.online",
                        "https://api.lookgen.online"       // ← novo
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false);
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // Normalize para barras / e garanta a barra final
        String location = "file:" + mediaRoot.replace("\\", "/") + "/";

        registry
                .addResourceHandler("/media/**")   // tudo que começa com /media/
                .addResourceLocations(location);   // aponta pra pasta do disco
    }

    @PostConstruct
    void debugMediaRoot() {
        // Maneira rápida e “barulhenta”:
        System.out.println("MEDIA ROOT → " + mediaRoot);

        // (opcional) Maneira “certa” com SLF4J:
        // log.info("MEDIA ROOT → {}", mediaRoot);
    }
}
