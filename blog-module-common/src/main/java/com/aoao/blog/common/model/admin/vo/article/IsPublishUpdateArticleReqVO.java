package com.aoao.blog.common.model.admin.vo.article;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author aoao
 * @create 2025-08-13-22:02
 */
@Data
public class IsPublishUpdateArticleReqVO {
    @NotNull
    private Long id;

    @NotNull
    private Boolean isPublish;
}
