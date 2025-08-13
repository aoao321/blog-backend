package com.aoao.blog.common.model.admin.vo.tag;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Size;
import java.util.List;


/**
 * @author aoao
 * @create 2025-07-22-9:33
 */
@Data
@ApiModel(value = "添加标签 VO")
public class AddTagReqVO {
    @Size(min = 1, max = 10, message = "标签数量必须在1-10之间")
    private List<String> tags;
}
