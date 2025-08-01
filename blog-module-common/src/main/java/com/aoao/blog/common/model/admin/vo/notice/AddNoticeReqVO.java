package com.aoao.blog.common.model.admin.vo.notice;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author aoao
 * @create 2025-07-30-16:25
 */
@Data
public class AddNoticeReqVO {
    @NotNull
    private String content;
}
