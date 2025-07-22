package com.aoao.blog.jwt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author aoao
 * @create 2025-07-13-18:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginTokenVo {
    /**
     * Token 值
     */
    private String token;
}
