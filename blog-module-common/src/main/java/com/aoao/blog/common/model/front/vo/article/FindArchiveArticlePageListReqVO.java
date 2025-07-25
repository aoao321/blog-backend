package com.aoao.blog.common.model.front.vo.article;

import com.aoao.blog.common.model.BasePageQuery;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "文章归档分页 VO")
public class FindArchiveArticlePageListReqVO extends BasePageQuery {
}