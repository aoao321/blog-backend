package com.aoao.blog.web.controller;

import com.aoao.blog.common.model.front.vo.commnet.FindQQUserInfoReqVO;
import com.aoao.blog.common.utils.Result;
import com.aoao.blog.web.service.CommentService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author aoao
 * @create 2025-08-12-17:29
 */
@RequestMapping("/comment")
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/qq/userInfo")
    @ApiOperation(value = "获取 QQ 用户信息")
    public Result findQQUserInfo(@RequestBody @Validated FindQQUserInfoReqVO findQQUserInfoReqVO) {
        return commentService.findQQUserInfo(findQQUserInfoReqVO);
    }
}
