package com.aoao.blog.common.model.admin.vo.notice;

import com.aoao.blog.common.model.BasePageQuery;
import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

/**
 * @author aoao
 * @create 2025-08-01-15:51
 */
@Data
public class FindNoticePageListReqVO extends BasePageQuery {

    private String content;

    private Boolean isShow;

    @PastOrPresent
    private LocalDate startDate;

    @FutureOrPresent
    private LocalDate endDate;


}
