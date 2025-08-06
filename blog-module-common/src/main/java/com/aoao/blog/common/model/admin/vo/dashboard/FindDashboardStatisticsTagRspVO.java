package com.aoao.blog.common.model.admin.vo.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author aoao
 * @create 2025-08-04-18:21
 */
@Data
@AllArgsConstructor
public class FindDashboardStatisticsTagRspVO {
     private List<String> tagNames;
     private List<Long> articleCounts;
}
