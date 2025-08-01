package com.aoao.blog.common.model.admin.vo.notice;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author aoao
 * @create 2025-08-01-15:17
 */
@Data
public class UpdateNoticeReqVO {
    @NotNull
    private Long id;
    private String content;

    private Boolean isShow;
}
