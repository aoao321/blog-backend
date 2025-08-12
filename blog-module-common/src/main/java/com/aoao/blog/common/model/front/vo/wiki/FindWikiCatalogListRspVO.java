package com.aoao.blog.common.model.front.vo.wiki;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindWikiCatalogListRspVO {

    /**
     * 知识库 ID
     */
    private Long id;

    private Long articleId;

    private String title;

    private Integer level;

    @JsonIgnore
    private Long parentId;

    /**
     * 二级目录
     */
    private List<FindWikiCatalogListRspVO> children;

}

