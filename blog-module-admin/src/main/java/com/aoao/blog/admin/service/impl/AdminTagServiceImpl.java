package com.aoao.blog.admin.service.impl;

import com.aoao.blog.common.domain.dos.ArticleTagRelDO;
import com.aoao.blog.common.domain.mapper.ArticleTagRelMapper;
import com.aoao.blog.common.model.admin.vo.tag.*;
import com.aoao.blog.admin.service.AdminTagService;
import com.aoao.blog.common.domain.dos.TagDO;
import com.aoao.blog.common.domain.mapper.TagMapper;
import com.aoao.blog.common.enums.ResponseCodeEnum;
import com.aoao.blog.common.exception.BizException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author aoao
 * @create 2025-07-22-9:36
 */
@Service
@Slf4j
public class AdminTagServiceImpl implements AdminTagService {

    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ArticleTagRelMapper articleTagRelMapper;

    @Override
    public void addTag(AddTagReqVO addTagReqVO) {
        // 根据传入的tag查询是否存在
        String addTagReqVOName = addTagReqVO.getName();
        TagDO tagDO = tagMapper.selectOne(new QueryWrapper<TagDO>()
                .eq("name", addTagReqVOName));
        if (tagDO == null) {
            // 插入数据
            tagDO = new TagDO();
            tagDO.setName(addTagReqVOName);
            tagMapper.insert(tagDO);
        }else {// 返回错误
            throw new BizException(ResponseCodeEnum.TAG_EXIST);
        }
    }

    @Override
    public void deleteTag(DeleteTagReqVO deleteTagReqVO) {
        Long id = deleteTagReqVO.getId();
        TagDO tagDO = tagMapper.selectById(id);
        if (tagDO != null) {
            // 校验该标签下是否有关联的文章，若有，则不允许删除，提示用户需要先删除标签下的文章
            ArticleTagRelDO articleTagRelDO = articleTagRelMapper.selectOne(new QueryWrapper<ArticleTagRelDO>()
                    .eq("tag_id", id));
            if (Objects.nonNull(articleTagRelDO)) {
                log.warn("==> 此标签下包含文章，无法删除，tagId: {}", id);
                throw new BizException(ResponseCodeEnum.TAG_CAN_NOT_DELETE);
            }
            tagMapper.deleteById(id);
        }else {
            throw new BizException(ResponseCodeEnum.TAG_NOT_EXIST);
        }
    }

    @Override
    public PageInfo<FindTagPageListRspVO> page(FindTagPageListReqVO findTagPageListReqVO) {
        // 根据条件查询
        String name = findTagPageListReqVO.getName();
        LocalDate endDate = findTagPageListReqVO.getEndDate();
        LocalDate startDate = findTagPageListReqVO.getStartDate();
        // 分页查询
        PageHelper.startPage(findTagPageListReqVO.getCurrent(), findTagPageListReqVO.getSize());
        List<TagDO> tagDOS = tagMapper.selectList(new QueryWrapper<TagDO>()
                .like(StringUtil.isNotEmpty(name), "name", name)
                .gt("create_time", startDate)
                .lt("create_time", endDate));
        // 封装成rspVO对象
        List<FindTagPageListRspVO> findTagPageListRspVOList = tagDOS.stream().map(tagDO -> {
            FindTagPageListRspVO vo = new FindTagPageListRspVO();
            BeanUtils.copyProperties(tagDO, vo);
            return vo;
        }).collect(Collectors.toList());
        PageInfo<FindTagPageListRspVO> pageInfo = new PageInfo<>(findTagPageListRspVOList);
        return pageInfo;
    }

    @Override
    public List<SelectTagListRspVO> selectTagList() {
        List<SelectTagListRspVO> rspVOS  = new ArrayList<>();
        for (TagDO tagDO : tagMapper.selectList(null)) {
            SelectTagListRspVO selectTagListRspVO = new SelectTagListRspVO();
            selectTagListRspVO.setLabel(tagDO.getName());
            selectTagListRspVO.setValue(tagDO.getId());
            rspVOS.add(selectTagListRspVO);
        }
        return rspVOS;
    }


}
