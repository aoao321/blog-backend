package com.aoao.blog.web.service;

import com.aoao.blog.common.model.front.vo.commnet.FindQQUserInfoReqVO;
import com.aoao.blog.common.utils.Result;

/**
 * @author aoao
 * @create 2025-08-12-17:30
 */
public interface CommentService {
    Result findQQUserInfo(FindQQUserInfoReqVO findQQUserInfoReqVO);
}
