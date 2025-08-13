package com.aoao.blog.admin.controller;

import com.aoao.blog.common.model.admin.vo.user.UpdateAdminUserPasswordReqVO;
import com.aoao.blog.admin.service.AdminUserService;
import com.aoao.blog.common.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author aoao
 * @create 2025-07-21-15:57
 */
@RestController
@Api("后台用户操作相关")
@RequestMapping("/admin")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;

    @PostMapping("/password/update")
    @ApiOperation("修改用户密码")
    public Result updatePassword(@RequestBody @Validated UpdateAdminUserPasswordReqVO reqVO) {
        adminUserService.updatePassword(reqVO);
        return Result.success();
    }

    @PostMapping("/user/info")
    @ApiOperation(value = "获取用户信息")
    public Result<String> findUserInfo() {
        return adminUserService.findUserInfo();
    }


}
