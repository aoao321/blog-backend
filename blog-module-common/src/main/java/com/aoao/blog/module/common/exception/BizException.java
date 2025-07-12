package com.aoao.blog.module.common.exception;

import com.aoao.blog.module.common.enums.ResponseCodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 自定义业务异常类
 *
 * @author aoao
 * @create 2025-07-11-10:02
 */
public class BizException extends BaseException {

    public BizException(ResponseCodeEnum codeEnum) {
        this.setErrorCode(codeEnum.getErrorCode());
        this.setErrorMessage(codeEnum.getErrorMessage());
    }


}
