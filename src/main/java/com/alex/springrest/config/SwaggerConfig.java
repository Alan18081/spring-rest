package com.alex.springrest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    Contact contact = new Contact("Alan Morgan", "http://alan.make.com", "alan@gmail.com");

    List<VendorExtension> vendorExtensions = new ArrayList<>();

    ApiInfo apiInfo = new ApiInfo(
            "My first Spring REST App",
            "Docs for spring rest app",
            "0.0.5",
            "Some terms",
            contact,
            "MIT",
            "MIT url",
            vendorExtensions
    );

    @Bean
    public Docket apiDocket() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .protocols(new HashSet<>(Arrays.asList("HTTP", "HTTPS")))
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.alex.springrest"))
                .paths(PathSelectors.any())
                .build();

        return docket;
    }

}
