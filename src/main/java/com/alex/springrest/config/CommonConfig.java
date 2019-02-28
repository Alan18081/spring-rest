package com.alex.springrest.config;

import com.alex.springrest.SpringApplicationContext;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

}
