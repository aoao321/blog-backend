package com.aoao.blog.admin.service.impl;

import com.aoao.blog.common.model.admin.vo.setting.FindBlogSettingsRspVO;
import com.aoao.blog.common.model.admin.vo.setting.UpdateBlogSettingsReqVO;
import com.aoao.blog.admin.service.AdminBlogSettingService;
import com.aoao.blog.common.domain.dos.BlogSettingsDO;
import com.aoao.blog.common.domain.mapper.BlogSettingMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author aoao
 * @create 2025-07-22-14:54
 */
@Service
public class AdminBlogSettingServiceImpl implements AdminBlogSettingService {

    @Autowired
    private BlogSettingMapper blogSettingMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSetting(UpdateBlogSettingsReqVO reqVO) {
        BlogSettingsDO settingsDO = new BlogSettingsDO();
        BeanUtils.copyProperties(reqVO, settingsDO);
        settingsDO.setId(1l);
        if (blogSettingMapper.selectById(1l)==null){
            blogSettingMapper.insert(settingsDO);
        }else {
            blogSettingMapper.updateById(settingsDO);
        }
    }

    @Override
    public FindBlogSettingsRspVO getDetail() {
        BlogSettingsDO settingsDO = blogSettingMapper.selectById(1l);
        FindBlogSettingsRspVO rspVO = new FindBlogSettingsRspVO();
        BeanUtils.copyProperties(settingsDO, rspVO);
        return rspVO;
    }
}
