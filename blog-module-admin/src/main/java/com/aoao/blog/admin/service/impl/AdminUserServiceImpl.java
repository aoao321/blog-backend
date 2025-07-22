package com.aoao.blog.admin.service.impl;

import com.aoao.blog.admin.model.vo.user.UpdateAdminUserPasswordReqVO;
import com.aoao.blog.admin.service.AdminUserService;
import com.aoao.blog.common.domain.dos.UserDO;
import com.aoao.blog.common.domain.mapper.UserMapper;
import com.aoao.blog.common.enums.ResponseCodeEnum;
import com.aoao.blog.common.exception.LoginException;
import com.aoao.blog.common.utils.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



/**
 * @author aoao
 * @create 2025-07-21-15:57
 */
@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void updatePassword(UpdateAdminUserPasswordReqVO reqVO) {
        String username = reqVO.getUsername();
        // 从数据库中获取用户的信息
        UserDO user = userMapper.selectOne(new QueryWrapper<UserDO>()
                .eq("username", username));
        if (user == null) {
            throw new LoginException(ResponseCodeEnum.USERNAME_NOT_EXIST);
        }
        // 加密密码存到数据库中
        String inputPassword = reqVO.getPassword();
        String encode = passwordEncoder.encode(inputPassword);
        // 更新数据库
        userMapper.update(null,
                new UpdateWrapper<UserDO>()
                        .set("password", encode)
                        .eq("username", username));
    }

    @Override
    public Result<String> findUserInfo() {
        // 从security获取上下文用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        return Result.success(name);

    }
}
