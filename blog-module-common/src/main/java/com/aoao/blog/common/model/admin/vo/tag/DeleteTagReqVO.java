package com.aoao.blog.common.model.admin.vo.tag;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author aoao
 * @create 2025-07-22-9:48
 */
@Data
public class DeleteTagReqVO {
    @NotBlank
    private Long Id;
}
