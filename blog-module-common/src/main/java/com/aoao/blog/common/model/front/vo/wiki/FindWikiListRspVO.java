package com.aoao.blog.common.model.front.vo.wiki;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author aoao
 * @create 2025-08-12-12:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindWikiListRspVO {

    private Long id;

    private String cover;

    private Long firstArticleId;

    private Boolean isTop;

    private String summary;

    private String title;
}
