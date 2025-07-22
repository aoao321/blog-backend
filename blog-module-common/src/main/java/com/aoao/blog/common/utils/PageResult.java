package com.aoao.blog.common.utils;

import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author aoao
 * @create 2025-07-21-20:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> extends Result<List<T>> {
    /**
     * 总记录数
     */
    private long total = 0L;

    /**
     * 每页显示的记录数，默认每页显示 10 条
     */
    private long size = 10L;

    /**
     * 当前页码
     */
    private long current;

    /**
     * 总页数
     */
    private long pages;

    public static <T> PageResult<T> success(PageInfo<T> pageInfo) {
        PageResult<T> result = new PageResult<>();
        result.total = pageInfo.getTotal();
        result.size = pageInfo.getSize();
        result.current = pageInfo.getPageNum();
        result.pages = pageInfo.getPages();
        result.setData(pageInfo.getList());
        result.setSuccess(true);
        result.setErrorCode(null);
        result.setMessage("请求成功");
        return result;
    }

}
