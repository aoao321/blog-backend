package com.aoao.blog.admin.service;

import com.aoao.blog.common.model.admin.vo.dashboard.FindDashboardPVRspVO;
import com.aoao.blog.common.model.admin.vo.dashboard.FindDashboardStatisticsInfoRspVO;

import java.time.LocalDate;
import java.util.Map;

/**
 * @author aoao
 * @create 2025-08-01-17:28
 */
public interface AdminDashBoardService {
    FindDashboardStatisticsInfoRspVO statistics();

    Map<LocalDate, Long> publishArticleStatistics();

    FindDashboardPVRspVO pvStatistics();
}
