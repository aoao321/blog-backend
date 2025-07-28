package com.aoao.blog.common.model.front.vo.category;

import com.aoao.blog.common.model.BasePageQuery;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author aoao
 * @create 2025-07-27-18:11
 */
@Data
public class FindArticleWithTypeCategoryVO extends BasePageQuery {
    @NotNull
    private Long id;
}
