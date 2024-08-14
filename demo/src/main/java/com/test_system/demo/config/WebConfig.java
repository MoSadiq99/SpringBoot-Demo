package com.test_system.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // Allows all origins; adjust as needed
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS") // Adjust methods as needed
                .allowedHeaders("*") // Allows all headers; adjust as needed
                .allowCredentials(true); // Allows credentials; set to false if not needed
    }
}
