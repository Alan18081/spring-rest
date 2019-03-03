package com.alex.springrest.config;

import com.alex.springrest.SpringApplicationContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class CommonConfig {

    @Bean
    public SpringApplicationContext springApplicationContext() {
        return new SpringApplicationContext();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public VelocityEngine velocityEngine() {
//        Properties p = new Properties();
//        p.setProperty("resource.loader", "class");
//        p.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
//        Velocity.init( p );
        return new VelocityEngine();
    }

}
