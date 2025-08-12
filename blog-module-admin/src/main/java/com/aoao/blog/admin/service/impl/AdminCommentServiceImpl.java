package com.aoao.blog.admin.service.impl;

import com.aoao.blog.admin.service.AdminCommentService;
import com.aoao.blog.common.domain.dos.CommentDO;
import com.aoao.blog.common.domain.mapper.CommentMapper;
import com.aoao.blog.common.enums.ResponseCodeEnum;
import com.aoao.blog.common.exception.BizException;
import com.aoao.blog.common.model.admin.vo.comment.DeleteCommentReqVO;
import com.aoao.blog.common.model.admin.vo.comment.ExamineCommentReqVO;
import com.aoao.blog.common.model.admin.vo.comment.FindCommentPageListReqVO;
import com.aoao.blog.common.model.admin.vo.comment.FindCommentPageListRspVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author aoao
 * @create 2025-08-11-17:01
 */
@Service
public class AdminCommentServiceImpl implements AdminCommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public PageInfo<FindCommentPageListRspVO> page(FindCommentPageListReqVO findCommentPageListReqVO) {
        // 分页
        PageHelper.startPage(findCommentPageListReqVO.getCurrent(), findCommentPageListReqVO.getSize());
        // 查询
        Integer status = findCommentPageListReqVO.getStatus();
        String routerUrl = findCommentPageListReqVO.getRouterUrl();
        LocalDate startDate = findCommentPageListReqVO.getStartDate();
        LocalDate endDate = findCommentPageListReqVO.getEndDate();

        List<CommentDO> commentDOS = commentMapper.selectList(new QueryWrapper<CommentDO>()
                .eq(Objects.nonNull(status), "status", status)
                .like(StringUtil.isNotEmpty(routerUrl), "router_url", routerUrl)
                .gt(startDate != null, "create_time", startDate)
                .lt(endDate != null, "create_time", endDate));

        // 封装成vo
        List<FindCommentPageListRspVO> vos = commentDOS.stream().map(commentDO -> {
            FindCommentPageListRspVO vo = new FindCommentPageListRspVO();
            BeanUtils.copyProperties(commentDO, vo);
            return vo;
        }).collect(Collectors.toList());
        return new PageInfo<>(vos);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(DeleteCommentReqVO deleteCommentReqVO) {
        // 查询评论
        Long id = deleteCommentReqVO.getId();
        CommentDO commentDO = commentMapper.selectById(id);
        if (Objects.isNull(commentDO)) {
            throw new BizException(ResponseCodeEnum.COMMENT_NOT_EXIST);
        }
        commentMapper.deleteById(id);

        Long replyCommentId = commentDO.getReplyCommentId();
        // 一级评论
        if (Objects.isNull(replyCommentId)) {
            // 删除子评论
            commentMapper.delete(new QueryWrapper<CommentDO>()
                    .eq("parent_comment_id", id));
        }
    }

    @Override
    public void examine(ExamineCommentReqVO examineCommentReqVO) {
        // 是否存在
        Long id = examineCommentReqVO.getId();
        CommentDO commentDO = commentMapper.selectById(id);
        if (Objects.isNull(commentDO)) {
            throw new BizException(ResponseCodeEnum.COMMENT_NOT_EXIST);
        }
        // 更新状态
        Integer status = examineCommentReqVO.getStatus();
        String reason = examineCommentReqVO.getReason();
        // 更新评论
        commentMapper.updateById(CommentDO.builder()
                .id(id)
                .status(status)
                .reason(reason)
                .updateTime(LocalDateTime.now())
                .build());
    }
}
