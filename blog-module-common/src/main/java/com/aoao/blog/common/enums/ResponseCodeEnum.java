package com.aoao.blog.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author aoao
 * @create 2025-07-11-9:27
 */
@Getter
public enum ResponseCodeEnum  {

    // ----------- 通用异常状态码 -----------
    SYSTEM_ERROR("10000", "出错啦，后台小哥正在努力修复中..."),

    // ----------- 业务异常状态码 -----------
    PRODUCT_NOT_FOUND("20000", "该产品不存在（测试使用）"),

    PARAM_NOT_VALID("10001","参数验证错误"),

    LOGIN_FAIL("20000", "登录失败"),
    UNAUTHORIZED("20002","用户未授权"),

    FORBIDDEN("20004", "该账号仅支持查询操作！"),

    USERNAME_OR_PWD_IS_NULL("20003","用户名或密码为空"),
    USERNAME_OR_PWD_ERROR("20001", "用户名或密码错误"),
    USERNAME_NOT_EXIST("20005", "用户名不存在"),

    CATEGORY_EXIST("30001", "该分类已经存在"),
    CATEGORY_NOT_EXIST("30002","该分类不存在" ),

    TAG_EXIST("30003", "该标签已经存在"),
    TAG_NOT_EXIST("30004","该标签不存在" ),

    FILE_NOT_EXIST("30005", "上传失败，请重新上传"),
    ARTICLE_NOT_EXIST("30006","文章不存在" ),
    CATEGORY_CAN_NOT_DELETE("30007", "该分类下存在文章，不允许删除"),
    TAG_CAN_NOT_DELETE("30008","该标签下存在文章，不允许删除" );

    ResponseCodeEnum(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    // 异常码
    private String errorCode;
    // 错误信息
    private String errorMessage;


}
