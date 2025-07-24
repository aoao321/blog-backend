package com.aoao.blog.common.model.admin.vo.tag;

import com.aoao.blog.common.model.BasePageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

/**
 * @author aoao
 * @create 2025-07-22-10:00
 */
@Data
@ApiModel("查询标签入参")
public class FindTagPageListReqVO extends BasePageQuery {
    /**
     * 分类名称
     */
    private String name;

    /**
     * 创建的起始日期
     */
    @PastOrPresent
    @ApiModelProperty("开始日期")
    private LocalDate startDate;

    /**
     * 创建的结束日期
     */
    @FutureOrPresent
    @ApiModelProperty("结束日期")
    private LocalDate endDate;
}
