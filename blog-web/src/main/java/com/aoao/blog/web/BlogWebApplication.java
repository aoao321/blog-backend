package com.aoao.blog.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.aoao.blog.admin",
        "com.aoao.blog.common",
        "com.aoao.blog.jwt",
        "com.aoao.blog.web"
})
 // 多模块项目中，必需手动指定扫描包
@CrossOrigin
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableScheduling
public class BlogWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogWebApplication.class, args);
    }

}
