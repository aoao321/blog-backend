package com.aoao.blog.web;

import com.aoao.blog.common.domain.mapper.UserMapper;
import com.aoao.blog.common.domain.mapper.UserRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class BlogWebApplicationTests {


    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;


    @Test
    void testMyBatisPlus() {
        //userMapper.insert(new UserDO(1l,"小敖兴","1243", new Date(),new Date(),false));
        //userMapper.selectById(1l);
        System.out.println(userRoleMapper.selectRoleNameByUserName("admin"));
    }

    @Test
    void testString() {
        String a = "1.txt";
        System.out.println(a.substring(a.lastIndexOf(".")));
    }

}
