package com.aoao.blog.common.model.front.vo.tag;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author aoao
 * @create 2025-07-24-13:52
 */
@Data
@AllArgsConstructor
public class FindTagListRspVO implements Serializable {
    private Long id;
    private String name;
}
