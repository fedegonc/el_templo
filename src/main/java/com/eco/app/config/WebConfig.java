package com.eco.app.config;

import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración web para la aplicación
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    /**
     * Habilita el filtro para soportar métodos HTTP como DELETE, PUT, etc.
     * a través del parámetro _method en formularios HTML
     */
    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }
    
    /**
     * Configura los formateadores de fecha y hora para la aplicación
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setDateFormatter(DateTimeFormatter.ISO_DATE);
        registrar.setTimeFormatter(DateTimeFormatter.ISO_TIME);
        registrar.registerFormatters(registry);
    }
}
