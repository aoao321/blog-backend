package com.aoao.blog.common.model.admin.vo.category;

import com.aoao.blog.common.model.BasePageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "查询分类分页数据入参 VO")
public class FindCategoryPageListReqVO extends BasePageQuery {

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