package com.aoao.blog.common.model.admin.vo.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author aoao
 * @create 2025-08-04-18:13
 */
@Data
@AllArgsConstructor
public class FindDashboardStatisticsCategoryRspVO {
    private String name;
    private Long value;
}
