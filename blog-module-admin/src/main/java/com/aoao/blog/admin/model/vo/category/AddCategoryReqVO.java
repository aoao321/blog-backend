package com.aoao.blog.admin.model.vo.category;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author aoao
 * @create 2025-07-21-19:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "添加分类 VO")
public class AddCategoryReqVO {

    @NotBlank(message = "分类名称不能为空")
    @Length(min = 1, max = 15, message = "分类名称字数限制 1 ~ 15 之间")
    private String name;

}