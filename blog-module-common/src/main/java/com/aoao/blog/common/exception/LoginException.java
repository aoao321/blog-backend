package com.aoao.blog.common.exception;

import com.aoao.blog.common.enums.ResponseCodeEnum;

/**
 * @author aoao
 * @create 2025-07-21-16:35
 */
public class LoginException extends BaseException{

    public LoginException(ResponseCodeEnum codeEnum) {
        this.setErrorCode(codeEnum.getErrorCode());
        this.setErrorMessage(codeEnum.getErrorMessage());
    }

}
