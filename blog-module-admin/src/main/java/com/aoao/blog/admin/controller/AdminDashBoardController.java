package com.aoao.blog.admin.controller;

import com.aoao.blog.admin.service.AdminDashBoardService;
import com.aoao.blog.common.model.admin.vo.dashboard.FindDashboardStatisticsInfoRspVO;
import com.aoao.blog.common.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

/**
 * @author aoao
 * @create 2025-08-01-17:27
 */
@RestController
@RequestMapping("admin/dashboard")
@Api(tags = "Admin 仪表盘")
public class AdminDashBoardController {

    @Autowired
    private AdminDashBoardService adminDashBoardService;

    @PostMapping("/statistics")
    @ApiOperation(value = "获取后台仪表盘基础统计信息")
    public Result<FindDashboardStatisticsInfoRspVO> findDashboardStatistics() {
        FindDashboardStatisticsInfoRspVO vo = adminDashBoardService.statistics();
        return Result.success(vo);
    }

    @PostMapping("/publishArticle/statistics")
    @ApiOperation(value = "获取后台仪表盘半年文章发布信息")
    public Result<Map<LocalDate, Long>> publishArticleStatistics() {
        Map<LocalDate, Long> map = adminDashBoardService.publishArticleStatistics();
        return Result.success(map);
    }
}
