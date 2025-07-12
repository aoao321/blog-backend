package com.aoao.blog.module.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * 自定义异常接口
 *
 * @author aoao
 * @create 2025-07-11-9:23
 */
@Getter
@Setter
public abstract class BaseException extends RuntimeException {

    // 异常码
    private String errorCode;
    // 错误信息
    private String errorMessage;

}
