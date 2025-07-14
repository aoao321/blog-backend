package com.aoao.blog.web;

import com.aoao.blog.common.domain.dos.UserDO;
import com.aoao.blog.common.domain.mapper.UserMapper;
import com.aoao.blog.common.domain.mapper.UserRoleMapper;
import com.aoao.blog.web.controller.HelloController;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
@Slf4j
class BlogWebApplicationTests {

    @Autowired
    private HelloController helloController;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Test
    void testLog() {
        System.out.println(helloController.hello("小敖兴321"));
    }

    @Test
    void testMyBatisPlus() {
        //userMapper.insert(new UserDO(1l,"小敖兴","1243", new Date(),new Date(),false));
        //userMapper.selectById(1l);
        System.out.println(userRoleMapper.selectRoleNameByUserName("admin"));
    }

}
