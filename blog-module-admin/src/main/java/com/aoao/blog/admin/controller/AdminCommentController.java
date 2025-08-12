package com.aoao.blog.admin.controller;

import com.aoao.blog.admin.service.AdminCommentService;
import com.aoao.blog.common.model.admin.vo.comment.DeleteCommentReqVO;
import com.aoao.blog.common.model.admin.vo.comment.ExamineCommentReqVO;
import com.aoao.blog.common.model.admin.vo.comment.FindCommentPageListReqVO;
import com.aoao.blog.common.model.admin.vo.comment.FindCommentPageListRspVO;
import com.aoao.blog.common.utils.PageResult;
import com.aoao.blog.common.utils.Result;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.simpleframework.xml.core.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author aoao
 * @create 2025-08-11-17:00
 */
@RestController
@RequestMapping("/admin/comment")
public class AdminCommentController {

    @Autowired
    private AdminCommentService adminCommentService;

    @PostMapping("/list")
    @ApiOperation("分页")
    public PageResult<FindCommentPageListRspVO> page(@RequestBody @Validated FindCommentPageListReqVO findCommentPageListReqVO) {
        PageInfo<FindCommentPageListRspVO> pageInfo = adminCommentService.page(findCommentPageListReqVO);
        return PageResult.success(pageInfo);
    }

    @PostMapping("/delete")
    @ApiOperation("删除")
    public Result delete(@RequestBody @Validated DeleteCommentReqVO deleteCommentReqVO) {
        adminCommentService.delete(deleteCommentReqVO);
        return Result.success();
    }

    @PostMapping("/examine")
    @ApiOperation("审核")
    public Result examine(@RequestBody @Validated ExamineCommentReqVO examineCommentReqVO) {
        adminCommentService.examine(examineCommentReqVO);
        return Result.success();
    }

}
