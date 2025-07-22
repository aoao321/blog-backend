package com.aoao.blog.admin.service;

import com.aoao.blog.admin.model.vo.user.UpdateAdminUserPasswordReqVO;
import com.aoao.blog.common.utils.Result;

import javax.validation.Valid;

/**
 * @author aoao
 * @create 2025-07-21-15:57
 */
public interface AdminUserService {
    void updatePassword(@Valid UpdateAdminUserPasswordReqVO reqVO);

    Result<String> findUserInfo();
}
