package com.aoao.blog.common.model.front.vo.article;

import com.aoao.blog.common.model.front.vo.category.FindCategoryListRspVO;
import com.aoao.blog.common.model.front.vo.tag.FindTagListRspVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindIndexArticlePageListRspVO implements Serializable {
    private Long id;
    private String cover;
    private String title;
    private LocalDateTime createTime;
    private String summary;
    /**
     * 文章分类
     */
    private FindCategoryListRspVO category;

    /**
     * 文章标签
     */
    private List<FindTagListRspVO> tags;
}