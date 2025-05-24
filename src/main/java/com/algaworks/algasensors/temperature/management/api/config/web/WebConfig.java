package com.algaworks.algasensors.temperature.management.api.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.format.FormatterRegistry;

@Configuration
public class WebConfig implements WebMvcConfigurer {


   @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToTSIDWebconverter());
    }

}
