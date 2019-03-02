package org.venkat.freshfruits;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@SpringBootApplication
public class FreshFruitsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FreshFruitsApplication.class, args);
    }

    @Configuration
    @EnableSwagger2
    public class SwaggerConfig {
        @Bean
        public Docket api() {
            return new Docket(DocumentationType.SWAGGER_2)
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("org.venkat.freshfruits"))
                    .paths(PathSelectors.any())
                    .build()
                    .apiInfo(apiInfo());
        }

        private ApiInfo apiInfo() {
            return new ApiInfo("Fresh Fruits API",
                    "APIs developed for the Fresh Fruits Chain of business",
                    "v1",
                    "urn:tos",
                    new Contact("Venkat Utla", "", "utla.venkat@gmail.com"),
                    "",
                    "",
                    new ArrayList<>());
        }
    }

}
