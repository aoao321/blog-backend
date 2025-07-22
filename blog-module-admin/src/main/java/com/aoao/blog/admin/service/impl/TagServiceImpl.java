package com.aoao.blog.admin.service.impl;

import com.aoao.blog.admin.model.vo.tag.AddTagReqVO;
import com.aoao.blog.admin.model.vo.tag.DeleteTagReqVO;
import com.aoao.blog.admin.model.vo.tag.FindTagPageListReqVO;
import com.aoao.blog.admin.model.vo.tag.FindTagPageListRspVO;
import com.aoao.blog.admin.service.TagService;
import com.aoao.blog.common.domain.dos.TagDO;
import com.aoao.blog.common.domain.mapper.TagMapper;
import com.aoao.blog.common.enums.ResponseCodeEnum;
import com.aoao.blog.common.exception.BizException;
import com.aoao.blog.common.utils.PageResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author aoao
 * @create 2025-07-22-9:36
 */
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

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
            tagMapper.deleteById(id);
        }else {
            throw new BizException(ResponseCodeEnum.TAG_NOT_EXIST);
        }
    }

    @Override
    public PageResult<FindTagPageListRspVO> page(FindTagPageListReqVO findTagPageListReqVO) {
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
        return PageResult.success(pageInfo);
    }


}
