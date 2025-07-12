package com.aoao.blog.web;

import com.aoao.blog.web.controller.HelloController;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class BlogWebApplicationTests {

    @Autowired
    private HelloController helloController;

    @Test
    void testLog() {
        System.out.println(helloController.hello("小敖兴321"));
    }


}
