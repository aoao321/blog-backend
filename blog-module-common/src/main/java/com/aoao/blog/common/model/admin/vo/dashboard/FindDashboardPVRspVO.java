package com.aoao.blog.common.model.admin.vo.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @author aoao
 * @create 2025-08-02-23:11
 */
@Data
@AllArgsConstructor
public class FindDashboardPVRspVO {
    private List<LocalDate> pvDates;
    private List<Long> pvCounts;
}
