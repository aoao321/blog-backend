package com.aoao.blog.web.controller;

import com.aoao.blog.common.domain.dos.NoticeDO;
import com.aoao.blog.common.domain.mapper.NoticeMapper;
import com.aoao.blog.common.model.front.vo.notice.FindNoticeDetailRsqVO;
import com.aoao.blog.common.utils.Result;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author aoao
 * @create 2025-08-01-16:28
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeMapper noticeMapper;

    @ApiOperation("查询")
    @PostMapping("/info")
    public Result<FindNoticeDetailRsqVO> notice() {
        List<NoticeDO> noticeDOS = noticeMapper.selectList(new QueryWrapper<NoticeDO>().eq("is_show", true));
        NoticeDO noticeDO = noticeDOS.get(0);
        FindNoticeDetailRsqVO vo = new FindNoticeDetailRsqVO();
        BeanUtils.copyProperties(noticeDO, vo);
        return Result.success(vo);

    }
}
