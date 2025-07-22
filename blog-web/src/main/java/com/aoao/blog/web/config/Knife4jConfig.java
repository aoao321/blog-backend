package com.aoao.blog.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @author aoao
 * @create 2025-07-11-11:57
 */
// web模块的配置类，类名和配置类bean名称都改下
@Configuration("webKnife4jConfig")
@EnableSwagger2WebMvc
public class Knife4jConfig {
    @Bean("webApi")
    public Docket createWebApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Web 前台接口")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.aoao.blog.web.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .title("Web 前台接口文档")
                        .version("1.0")
                        .build());
    }
    @Bean("adminApi")
    public Docket createAdminApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Admin 后台接口")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.aoao.blog.admin.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .title("Admin 后台接口文档")
                        .version("1.0")
                        .build());
    }
}
