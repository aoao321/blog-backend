package com.aoao.blog.admin.service;

import com.aoao.blog.common.model.admin.vo.setting.FindBlogSettingsRspVO;
import com.aoao.blog.common.model.admin.vo.setting.UpdateBlogSettingsReqVO;

import javax.validation.Valid;

/**
 * @author aoao
 * @create 2025-07-22-14:54
 */
public interface AdminBlogSettingService {
    void updateSetting(@Valid UpdateBlogSettingsReqVO reqVO);

    FindBlogSettingsRspVO getDetail();
}
