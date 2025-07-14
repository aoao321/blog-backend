package com.aoao.jwt.service.Impl;

import com.aoao.blog.common.domain.dos.UserDO;
import com.aoao.blog.common.domain.dos.UserRoleDO;
import com.aoao.blog.common.domain.mapper.UserMapper;
import com.aoao.blog.common.domain.mapper.UserRoleMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author aoao
 * @create 2025-07-13-17:21
 */
@Slf4j
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 根据username查询数据库
        UserDO user = userMapper.selectOne(new QueryWrapper<UserDO>().eq("username", username));
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        // TODO: 获取权限认证信息
        // 根据用户名查询角色信息,获得用户权限集合
        List<String> roleList = userRoleMapper.selectRoleNameByUserName(username);
        // 将集合中的元素转换为authorities
        List<SimpleGrantedAuthority> authorities = roleList
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission))
                .collect(Collectors.toList());

        // log.info("user:{}", user);
        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }

}
