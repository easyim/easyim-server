package com.easyim.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    // config
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.easyim.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    // info
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("EASYIM HTTP-APIS")
                .contact(new Contact("kong", "", ""))
                .version("1.0")
                .description("description")
                .build();
    }

}
