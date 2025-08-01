package com.aoao.blog.common.model.admin.vo.notice;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author aoao
 * @create 2025-08-01-16:08
 */
@Data
public class FindNoticePageListRspVO {

    private Long id;
    private String content;
    private Boolean isShow;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
