package com.aoao.blog.common.model.admin.vo.comment;


import com.aoao.blog.common.model.BasePageQuery;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "查询评论分页数据入参 VO")
public class FindCommentPageListReqVO extends BasePageQuery {

    /**
     * 路由地址
     */
    private String routerUrl;

    /**
     * 发布的起始日期
     */
    @PastOrPresent
    private LocalDate startDate;

    /**
     * 发布的结束日期
     */
    @FutureOrPresent
    private LocalDate endDate;

    /**
     * 状态
     */
    private Integer status;
}

