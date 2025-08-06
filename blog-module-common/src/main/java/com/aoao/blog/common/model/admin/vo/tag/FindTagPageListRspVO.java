package com.aoao.blog.common.model.admin.vo.tag;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author aoao
 * @create 2025-07-22-10:01
 */
@Data
@ApiModel("查询标签出参")
public class FindTagPageListRspVO {
    /**
     * 分类 ID
     */
    private Long id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    private Long articlesTotal;
}
