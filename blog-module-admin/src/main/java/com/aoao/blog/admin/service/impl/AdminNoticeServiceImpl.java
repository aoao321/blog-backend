package com.aoao.blog.admin.service.impl;

import com.aoao.blog.admin.service.AdminNoticeService;
import com.aoao.blog.common.domain.dos.ArticleDO;
import com.aoao.blog.common.domain.dos.NoticeDO;
import com.aoao.blog.common.domain.mapper.NoticeMapper;
import com.aoao.blog.common.enums.ResponseCodeEnum;
import com.aoao.blog.common.exception.BizException;
import com.aoao.blog.common.model.admin.vo.notice.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.baomidou.mybatisplus.extension.toolkit.SimpleQuery.selectList;

/**
 * @author aoao
 * @create 2025-07-30-16:20
 */
@Service
public class AdminNoticeServiceImpl implements AdminNoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    public void add(AddNoticeReqVO addNoticeReqVO) {
        NoticeDO noticeDO = new NoticeDO();
        BeanUtils.copyProperties(addNoticeReqVO, noticeDO);
        noticeMapper.insert(noticeDO);
    }

    @Override
    public void update(UpdateNoticeReqVO updateNoticeReqVO) {
        NoticeDO noticeDO = new NoticeDO();
        BeanUtils.copyProperties(updateNoticeReqVO, noticeDO);
        Boolean isShow = updateNoticeReqVO.getIsShow();
        if (isShow == null || isShow == false) {
            noticeMapper.updateById(noticeDO);
            return;
        }

        // 查询isShow是否超过
        long showingCount = noticeMapper.selectCount(new QueryWrapper<NoticeDO>()
                .eq("is_show", true));
        // 判断是否超过限制
        if (showingCount >= 1) {
            throw new BizException(ResponseCodeEnum.NOTICE_MAX);
        }
        // 没超过限制，允许设置为显示
        noticeMapper.updateById(noticeDO);

    }


    @Override
    public void delete(DeleteNoticeReqVO deleteNoticeReqVO) {
        noticeMapper.deleteById(deleteNoticeReqVO.getId());
    }

    @Override
    public PageInfo<FindNoticePageListRspVO> list(FindNoticePageListReqVO reqVO) {
        // 分页查询
        PageHelper.startPage(reqVO.getCurrent(), reqVO.getSize());
        // 查询
        String content = reqVO.getContent();
        LocalDate startDate = reqVO.getStartDate();
        LocalDate endDate = reqVO.getEndDate();
        Boolean isShow = reqVO.getIsShow();
        List<NoticeDO> noticeDOS = noticeMapper.selectList(new QueryWrapper<NoticeDO>()
                .like(StringUtil.isNotEmpty(content), "content", content)
                .eq(isShow != null, "is_show", isShow)
                .gt(startDate != null, "create_time", startDate)
                .lt(endDate != null, "create_time", endDate));
        List<FindNoticePageListRspVO> vos = noticeDOS.stream().map(noticeDO -> {
            FindNoticePageListRspVO findNoticePageListRspVO = new FindNoticePageListRspVO();
            BeanUtils.copyProperties(noticeDO, findNoticePageListRspVO);
            return findNoticePageListRspVO;
        }).collect(Collectors.toList());
        PageInfo<FindNoticePageListRspVO> pageInfo = new PageInfo<>(vos);
        return pageInfo;
    }
}
