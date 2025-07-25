package com.aoao.blog.common.model.front.vo.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author aoao
 * @create 2025-07-24-13:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindCategoryListRspVO implements Serializable {
    private Long id;
    private String name;
    private Long articlesTotal;
}
