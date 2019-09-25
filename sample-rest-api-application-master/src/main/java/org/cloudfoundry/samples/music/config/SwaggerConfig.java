package org.cloudfoundry.samples.music.config;

import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Documentation;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.swagger2.configuration.Swagger2DocumentationConfiguration;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2Mapper;
import springfox.documentation.swagger2.mappers.ServiceModelToSwagger2MapperImpl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableSwagger2
@Import({Swagger2DocumentationConfiguration.class})
public class SwaggerConfig {

    @Value("classpath:/config/swagger.yml")
    private Resource swaggerResource;

    @Bean
    public ServiceModelToSwagger2Mapper mapper() {
        return new  ServiceModelToSwagger2MapperImpl() {
            public Swagger mapDocumentation(Documentation from) {
                try {
                    String swaggerContent = StreamUtils.copyToString(SwaggerConfig.this.swaggerResource.getInputStream(), StandardCharsets.UTF_8);
                    return new SwaggerParser().parse(swaggerContent);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
