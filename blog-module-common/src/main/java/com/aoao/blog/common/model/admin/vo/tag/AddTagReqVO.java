package com.aoao.blog.common.model.admin.vo.tag;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


/**
 * @author aoao
 * @create 2025-07-22-9:33
 */
@Data
@ApiModel(value = "添加标签 VO")
public class AddTagReqVO {
    @Length(min = 1,max = 20,message = "标签长度限制在 1~20 之间")
    private String name;
}
