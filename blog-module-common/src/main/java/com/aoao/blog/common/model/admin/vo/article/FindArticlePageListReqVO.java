package com.aoao.blog.common.model.admin.vo.article;

import com.aoao.blog.common.model.BasePageQuery;
import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

/**
 * @author aoao
 * @create 2025-07-22-22:16
 */
@Data
public class FindArticlePageListReqVO extends BasePageQuery {

    /**
     * 文章标题
     */
    private String title;

    /**
     * 发布的起始日期
     */
    @PastOrPresent
    private LocalDate startDate;

    /**
     * 发布的结束日期
     */
    @FutureOrPresent
    private LocalDate endDate;
}
