package com.aoao.blog.common.model.admin.vo.tag;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author aoao
 * @create 2025-07-22-9:48
 */
@Data
public class DeleteTagReqVO {
    @NotNull(message = "ID不能为空")
    private Long Id;
}
