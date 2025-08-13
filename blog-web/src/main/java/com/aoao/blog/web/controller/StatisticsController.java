package com.aoao.blog.web.controller;

import com.aoao.blog.admin.service.AdminDashBoardService;
import com.aoao.blog.common.model.admin.vo.dashboard.FindDashboardStatisticsInfoRspVO;
import com.aoao.blog.common.utils.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author aoao
 * @create 2025-08-13-21:39
 */
@RequestMapping("/statistics")
@RestController
public class StatisticsController {

    @Autowired
    private AdminDashBoardService adminDashBoardService;

    @PostMapping("/info")
    @ApiOperation(value = "获取后台仪表盘基础统计信息")
    public Result<FindDashboardStatisticsInfoRspVO> findDashboardStatistics() {
        FindDashboardStatisticsInfoRspVO vo = adminDashBoardService.statistics();
        return Result.success(vo);
    }
}
